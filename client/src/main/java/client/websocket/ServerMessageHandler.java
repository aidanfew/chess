package client.websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public interface ServerMessageHandler {
    void notifyLoadGame(LoadGameMessage serverMessage);

    void notifyError(ErrorMessage serverMessage);

    void notifyNotification(NotificationMessage serverMessage);


}
