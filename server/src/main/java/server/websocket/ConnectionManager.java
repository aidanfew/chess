package server.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.common.WebSocketSession;
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
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        String message = notificationMessage.actionNotification(userName, action);
        if (session.isOpen()) {
            String connectMessage = new Gson().toJson(serverMessage);
            session.getRemote().sendString(connectMessage);
            System.out.println(connections);
            for (WebSocketSession s : connections.values()) {
                if (!s.equals(session)) {
                    s.getRemote().sendString(message);
                    System.out.println(message);
                }
            }
        }
    }
}
