package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthSqlDAO;
import dataaccess.DataAccessException;
import dataaccess.GameSqlDAO;
import dataaccess.UserSqlDAO;
import exception.ResponseException;
import io.javalin.websocket.*;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.swing.*;
import java.io.IOException;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameSqlDAO gameSqlDAO = new GameSqlDAO();
    private final AuthSqlDAO authSqlDAO = new AuthSqlDAO();
    private final ArrayList<Integer> endedGames = new ArrayList<>();

    public WebSocketHandler() throws DataAccessException {
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        connections.remove((WebSocketSession) wsCloseContext.session);
        System.out.println("Websocket closed");
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {
        System.out.println("Websocket connected");
        wsConnectContext.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(wsMessageContext.message(), UserGameCommand.class);
            if (gameSqlDAO.getGame(userGameCommand.getGameID()) == null || authSqlDAO.getAuth(userGameCommand.getAuthToken()) == null) {
                if (gameSqlDAO.getGame(userGameCommand.getGameID()) == null) {
                    error((WebSocketSession) wsMessageContext.session, "invalid Game ID");
                } else {
                    error((WebSocketSession) wsMessageContext.session, "user unauthorized");
                }
            } else {
                switch (userGameCommand.getCommandType()) {
                    case CONNECT -> connect((WebSocketSession) wsMessageContext.session,
                            gameSqlDAO.getGame(userGameCommand.getGameID()).game(),
                            authSqlDAO.getAuth(userGameCommand.getAuthToken()).username());
                    case LEAVE -> leave((WebSocketSession) wsMessageContext.session,
                            authSqlDAO.getAuth(userGameCommand.getAuthToken()).username(),
                            gameSqlDAO.getGame(userGameCommand.getGameID()));
                    case MAKE_MOVE -> makeMove((WebSocketSession) wsMessageContext.session,
                            authSqlDAO.getAuth(userGameCommand.getAuthToken()).username(),
                            convertCommandMakeMove(wsMessageContext).sendMove(),
                            gameSqlDAO.getGame(userGameCommand.getGameID()));
                    case RESIGN -> resign((WebSocketSession) wsMessageContext.session,
                            authSqlDAO.getAuth(userGameCommand.getAuthToken()).username(),
                            gameSqlDAO.getGame(userGameCommand.getGameID()));
                }
            }

            } catch(Exception e){
                e.printStackTrace();
        }
    }

    private void connect(WebSocketSession session, ChessGame game, String userName) throws IOException {
        connections.add(session);
        ServerMessage serverMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        connections.broadcastConnect(session, serverMessage, userName, "connected");
    }

    private void error(WebSocketSession session, String message) throws IOException {
        connections.sendError(session, "\u001B[31mError: " + message + "\u001B[0m");
    }

    private void leave(WebSocketSession session, String userName, GameData gameData) throws Exception {
        if (Objects.equals(userName, gameData.whiteUsername())) {
            gameSqlDAO.setUserToNull(gameData.gameID(), ChessGame.TeamColor.WHITE);
        } else {
            gameSqlDAO.setUserToNull(gameData.gameID(), ChessGame.TeamColor.BLACK);
        }
        connections.remove(session);
        connections.broadcastLeave(session, userName, "left");
    }

    private void resign(WebSocketSession session, String userName, GameData gameData) throws IOException {
        if (endedGames.contains(gameData.gameID())) {
            error(session, "Game is already over");
            return;
        }
        if (!Objects.equals(userName, gameData.whiteUsername()) && !Objects.equals(userName, gameData.blackUsername())) {
            error(session, "You are not in play");
            return;
        }
        connections.broadcastGeneralNotification(session, userName + " has resigned");
        endedGames.add(gameData.gameID());
    }

    private void makeMove(WebSocketSession session, String userName, ChessMove move, GameData gameData) throws IOException {
        ChessGame game = gameData.game();
        if (endedGames.contains(gameData.gameID())) {
            error(session, "Game is over");
            return;
        }
        if (!Objects.equals(userName, gameData.whiteUsername()) && !Objects.equals(userName, gameData.blackUsername())) {
            error(session, "You are not in play");
            return;
        }
        if ((Objects.equals(userName, gameData.whiteUsername()) && !Objects.equals(ChessGame.TeamColor.WHITE, gameData.game().getTeamTurn())) ||
                (Objects.equals(userName, gameData.blackUsername()) && !Objects.equals(ChessGame.TeamColor.BLACK, gameData.game().getTeamTurn()))) {
            error(session, "Not your turn");
            return;
        }
        try {
            game.setBoard(gameData.game().getBoard());
            game.makeMove(move);
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), game);
            gameSqlDAO.replaceGame(gameData.gameID(), null, newGameData);
            connections.broadcastMove(session, userName, "moved", reconvertMove(move));
            connections.broadcastBoard(session, newGameData.game());
            if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                connections.broadcastGeneralNotification(session, "WHITE is in check");
            } else if (game.isInCheck(ChessGame.TeamColor.BLACK)){
                connections.broadcastGeneralNotification(session, "BLACK is in check");
            } else if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                connections.broadcastMate(session, "check", ChessGame.TeamColor.WHITE);
                endedGames.add(gameData.gameID());
            } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                connections.broadcastMate(session, "check", ChessGame.TeamColor.BLACK);
                endedGames.add(gameData.gameID());
            } else if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                connections.broadcastMate(session, "stale", ChessGame.TeamColor.BLACK);
                endedGames.add(gameData.gameID());
            } else if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                connections.broadcastMate(session, "stale", ChessGame.TeamColor.BLACK);
                endedGames.add(gameData.gameID());
            }
        } catch (Exception e) {
            error(session, "invalid move");
        }
    }

    private String reconvertMove(ChessMove move) {
        String col = columnToFile(move.getEndPosition().getColumn());
        String row = columnToFile(move.getEndPosition().getRow());
        return col + row;
    }

    private String columnToFile(int col) {
        return switch (col) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> "i";
        };
    }

    private MakeMoveCommand convertCommandMakeMove(WsMessageContext context) {
        return new Gson().fromJson(context.message(), MakeMoveCommand.class);
    }
}
