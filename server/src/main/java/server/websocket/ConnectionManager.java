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
import java.net.http.WebSocket;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<WebSocketSession, WebSocketSession> connections = new ConcurrentHashMap<>();

    public void add(WebSocketSession session) {
        connections.put(session, session);
    }

    public void remove(WebSocketSession session) {
        connections.remove(session, session);
    }

    public void broadcast(WebSocketSession excludeSession, ServerMessage serverMessage) throws ResponseException, IOException {
        String message = serverMessage.toString();
        for (WebSocketSession session : connections.values()) {
            if (session.isOpen()) {
                if (!session.equals(excludeSession)) {
                    session.getRemote().sendString(message);
                    System.out.println(message);
                }
            }
        }
    }

    public void broadcastConnect(WebSocketSession session, ServerMessage serverMessage, String userName, String action) throws IOException {
        if (session.isOpen()) {
            String connectMessage = new Gson().toJson(serverMessage);
            session.getRemote().sendString(connectMessage);

            String message = actionNotification(userName, action);
            NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            for (WebSocketSession s : connections.values()) {
                if (!s.equals(session)) {
                    var json = new Gson().toJson(notificationMessage);
                    s.getRemote().sendString(json);
                }
            }
        }
    }

    public void broadcastLeave(WebSocketSession session, String userName, String action) throws IOException {
        String message = actionNotification(userName, action);
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        if (session.isOpen()) {
            for (WebSocketSession s : connections.values()) {
                if (!s.equals(session)) {
                    String json = new Gson().toJson(notificationMessage);
                    s.getRemote().sendString(json);
                }
            }
            connections.remove(session);
        }
    }

    public void sendError(WebSocketSession session, String errorMessage) throws IOException {
        if (session.isOpen()) {
            ErrorMessage errorType = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
            String json = new Gson().toJson(errorType);
            session.getRemote().sendString(json);
        }
    }

    public void broadcastMove(WebSocketSession session, String userName, String action, String endMove) throws IOException {
        String message = actionNotification(userName, action) + " to " + endMove;
        if (session.isOpen()) {
            NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            String json = new Gson().toJson(notificationMessage);
            for (WebSocketSession s : connections.values()) {
                if (!s.equals(session)) {
                    s.getRemote().sendString(json);
                }
            }
        }
    }

    public void broadcastBoard(WebSocketSession session, ChessGame game) throws IOException {
        if (session.isOpen()) {
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            String json = new Gson().toJson(loadGameMessage);
            for (WebSocketSession s : connections.values()) {
                s.getRemote().sendString(json);
            }
        }
    }

    public void broadcastMate(WebSocketSession session, String type, ChessGame.TeamColor color) throws IOException {
        if (session.isOpen()) {
            String message = String.format("%s is in %smate", color.toString(), type);
            NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            String json = new Gson().toJson(notificationMessage);
            for (WebSocketSession s : connections.values()) {
                s.getRemote().sendString(json);
            }
        }
    }

    public void broadcastGeneralNotification(WebSocketSession session, String message) throws IOException {
        if (session.isOpen()) {
            NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            String json = new Gson().toJson(notificationMessage);
            for (WebSocketSession s : connections.values()) {
                s.getRemote().sendString(json);
            }
        }
    }

    public String actionNotification(String userName, String action) {
        return userName + " has " + action;
    }
}
