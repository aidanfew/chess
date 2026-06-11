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
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameSqlDAO gameSqlDAO = new GameSqlDAO();
    private final AuthSqlDAO authSqlDAO = new AuthSqlDAO();

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
                    error((WebSocketSession) wsMessageContext.session, "Error: invalid Game ID");
                } else {
                    error((WebSocketSession) wsMessageContext.session, "Error: user unauthorized");
                }
            } else {
                switch (userGameCommand.getCommandType()) {
                    case CONNECT -> connect((WebSocketSession) wsMessageContext.session,
                            gameSqlDAO.getGame(userGameCommand.getGameID()).game(),
                            authSqlDAO.getAuth(userGameCommand.getAuthToken()).username());
                    case LEAVE -> leave((WebSocketSession) wsMessageContext.session,
                            authSqlDAO.getAuth(userGameCommand.getAuthToken()).username());
                    case MAKE_MOVE -> makeMove((WebSocketSession) wsMessageContext.session,
                            authSqlDAO.getAuth(userGameCommand.getAuthToken()).username(),
                            convertCommandMakeMove(wsMessageContext).dumpMove());
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
        connections.sendError(session, message);
    }

    private void leave(WebSocketSession session, String userName) throws IOException {
        connections.remove(session);
        connections.broadcastLeave(session, userName, "left");
    }

    private void makeMove(WebSocketSession session, String userName, ChessMove move, ) throws IOException {

    }

    private MakeMoveCommand convertCommandMakeMove(WsMessageContext context) {
        return new Gson().fromJson(String.valueOf(context), MakeMoveCommand.class);
    }
}
