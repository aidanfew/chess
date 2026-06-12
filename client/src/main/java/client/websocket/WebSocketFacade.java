package client.websocket;

import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;

public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler serverMessageHandler;
    int currentGameID;

    public WebSocketFacade(String url, ServerMessageHandler serverMessageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String message) {
                    ServerMessage type = new Gson().fromJson(message, ServerMessage.class);
                    if (type.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
                        LoadGameMessage gameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                        serverMessageHandler.notifyLoadGame(gameMessage);
                    } else if (type.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
                        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                        serverMessageHandler.notifyError(errorMessage);
                    } else {
                        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                        serverMessageHandler.notifyNotification(notificationMessage);
                    }
                }
            });
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void webSocketFacadeConnect(String authToken, String gameID) throws ResponseException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, Integer.parseInt(gameID));
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
            currentGameID = Integer.parseInt(gameID);
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }

    public void webSocketFacadeLeave(String authToken) throws ResponseException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, currentGameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }

    public void webSocketFacadeMakeMove(String authToken, ChessMove move) throws ResponseException {
        try {
            var makeMoveCommand = new MakeMoveCommand(MakeMoveCommand.CommandType.MAKE_MOVE, authToken, currentGameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveCommand));
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }

    public void webSocketFacadeResign(String authToken) throws ResponseException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, currentGameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }


}
