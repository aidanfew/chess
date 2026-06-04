package server;

public class ServerMain {

    public static void main(String[] args) {
        Server server = new Server();
        server.run(5000);

        System.out.println("♕ 240 Chess Server");

    }
}
