package websocket.messages;

public class NotificationMessage extends ServerMessage {

    public NotificationMessage(ServerMessageType type) {
        super(type);
    }

    public String actionNotification(String userName, String action) {
        return userName + " has " + action;
    }
}
