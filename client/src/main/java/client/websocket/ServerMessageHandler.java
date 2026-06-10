package client.websocket;

import websocket.messages.ServerMessage;

public interface ServerMessageHandler {
    void notifyLoadGame(ServerMessage serverMessage);

    void notifyError(ServerMessage serverMessage);

    void notifyNotification(ServerMessage serverMessage);


}
