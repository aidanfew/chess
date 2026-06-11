package client.websocket;

import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public interface ServerMessageHandler {
    void notifyLoadGame(LoadGameMessage serverMessage);

    void notifyError(ServerMessage serverMessage);

    void notifyNotification(ServerMessage serverMessage);


}
