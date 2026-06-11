package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.GameSqlDAO;
import exception.ResponseException;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import javax.swing.*;
import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private GameSqlDAO gameSqlDAO = new GameSqlDAO();

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
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
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect((WebSocketSession) wsMessageContext.session, gameSqlDAO.getGame(userGameCommand.getGameID()).game());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect(WebSocketSession session, ChessGame game) throws IOException {
        ServerMessage serverMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        connections.broadcastConnect(session, serverMessage);
    }
}
