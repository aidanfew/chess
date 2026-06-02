package client;

import exception.ResponseException;
import results.LoginResult;
import results.RegisterResult;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.print("Welcome to 240 chess. Log in or register to get started.\n");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equalsIgnoreCase("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n\u001B[0m>>> \033[32m");
    }

    public String eval(String input) {
        try {
            String[] tokens = input.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.SIGNEDIN;
            LoginResult result = server.facadeLogin(params[0], params[1]);
            return String.format("You signed in as %s", result.username());
        }
        throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mExpected: <your_username> <your_password>\u001B[0m");
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            state = State.SIGNEDIN;
            RegisterResult result = server.facadeRegister(params[0], params[1], params[2]);
            return String.format("You have successfully registered as %s", result.username());
        }
        throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: Expected <username> <password> <email>\u001B[0m");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    \u001B[33mregister <USERNAME> <PASSWORD> <EMAIL>\u001B[0m - to create an account
                    \u001B[34mlogin <USERNAME> <PASSWORD>\u001B[0m - to play chess
                    \u001B[33mquit\u001B[0m - to exit the program
                    \u001B[34mhelp\u001B[0m - for possible commands
                    """;
        } else {
            return null;
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mYou must sign in");
        }
    }
}
