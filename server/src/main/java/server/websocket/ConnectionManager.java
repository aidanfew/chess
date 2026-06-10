package server.websocket;

import exception.ResponseException;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import websocket.messages.ServerMessage;


import java.io.IOException;
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
}
