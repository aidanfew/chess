package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<WebSocketSession, WebSocketSession> connections = new ConcurrentHashMap<>();

    public void add(WebSocketSession session) {
        connections.put(session, session);
    }

    public void remove(WebSocketSession session) {
        connections.remove(session, session);
    }

    //send load game back to root
    public void broadcastConnect(WebSocketSession session, ServerMessage serverMessage) throws IOException {
        if (session.isOpen()) {
            String connectMessage = new Gson().toJson(serverMessage);
            session.getRemote().sendString(connectMessage);
        }
    }

    //send notification to everyone but root in the game
    public void broadcastAllButRoot(WebSocketSession session, String userName, String action,
                                    ArrayList<WebSocketSession> sessionList) throws IOException {
        String message = actionNotification(userName, action);
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        for (WebSocketSession s : sessionList) {
            if (s.isOpen()) {
                if (!s.equals(session)) {
                    String json = new Gson().toJson(notificationMessage);
                    s.getRemote().sendString(json);
                }
            }
        }
    }

    //send error to root only
    public void sendError(WebSocketSession session, String errorMessage) throws IOException {
        if (session.isOpen()) {
            ErrorMessage errorType = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
            String json = new Gson().toJson(errorType);
            session.getRemote().sendString(json);
        }
    }

    //send move to all but root
    public void broadcastMove(WebSocketSession session, String userName, String action, String endMove,
                              ArrayList<WebSocketSession> sessionList) throws IOException {
        String message = actionNotification(userName, action) + " to " + endMove;
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        String json = new Gson().toJson(notificationMessage);
        for (WebSocketSession s : sessionList) {
            if (s.isOpen()) {
                if (!s.equals(session)) {
                    s.getRemote().sendString(json);
                }
            }
        }
    }

    public void broadcastBoard(WebSocketSession session, ChessGame game, ArrayList<WebSocketSession> sessionList) throws IOException {
        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String json = new Gson().toJson(loadGameMessage);
        for (WebSocketSession s : sessionList) {
            if (s.isOpen()) {
                s.getRemote().sendString(json);
            }
        }
    }

    public void broadcastMate(WebSocketSession session, String type, ChessGame.TeamColor color,
                              ArrayList<WebSocketSession> sessionList) throws IOException {
        String message = String.format("%s is in %smate", color.toString(), type);
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        String json = new Gson().toJson(notificationMessage);
        for (WebSocketSession s : sessionList) {
            if (s.isOpen()) {
                s.getRemote().sendString(json);
            }
        }
    }

    public void broadcastGeneralNotification(WebSocketSession session, String message,
                                             ArrayList<WebSocketSession> sessionList) throws IOException {
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        String json = new Gson().toJson(notificationMessage);
        for (WebSocketSession s : sessionList) {
            if (s.isOpen()) {
                s.getRemote().sendString(json);
            }
        }
    }

    public String actionNotification(String userName, String action) {
        return userName + " has " + action;
    }
}
