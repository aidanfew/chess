package client;

import chess.ChessBoard;
import chess.ChessGame;
import exception.ResponseException;
import results.*;
import server.ServerFacade;
import ui.ManifestBoard;

import java.util.*;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken;
    private final HashMap<String, ListGamesHelperResult> gameHashMap = new HashMap<>();

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
        System.out.print("\n\u001B[0m" + state + ">>> \033[32m");
    }

    public String eval(String input) {
        try {
            String[] tokens = input.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state == State.SIGNEDOUT) {
                return switch (cmd) {
                    case "login" -> login(params);
                    case "register" -> register(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            } else if (state == State.SIGNEDIN) {
                return switch (cmd) {
                    case "logout" -> logout();
                    case "create" -> createGame(params);
                    case "quit" -> "quit";
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
                    case "observe" -> observe(params);
                    default -> help();
                };
            } else {
                return switch (cmd) {
                    case "quit" -> "quit";
                    case "return" -> returnToLogin();
                    default -> help();
                };
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            try {
                LoginResult result = server.facadeLogin(params[0], params[1]);
                authToken = result.authToken();
                state = State.SIGNEDIN;
                return String.format("You signed in as %s\n" + help(), result.username());
            } catch (Exception e) {
                throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: unregistered user");
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: Expected <your username> <your password>\u001B[0m");
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            try {
                RegisterResult result = server.facadeRegister(params[0], params[1], params[2]);
                authToken = result.authToken();
                state = State.SIGNEDIN;
                return String.format("You have successfully registered as %s\n" + help(), result.username());
            } catch (Exception e) {
                throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: username already taken");
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: Expected <username> <password> <email>\u001B[0m");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        try {
            server.facadeLogout(authToken);
            state = State.SIGNEDOUT;
            return "You have successfully logged out" + help();
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
        }
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            try {
                CreateGameResult result = server.facadeCreateGame(params[0], authToken);
                return String.format("You have successfully created game " + params[0]);
            } catch (Exception e) {
                throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: Expected <game name>\u001B[0m");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        StringBuilder string = new StringBuilder();
        try {
            ListGamesResult result = server.facadeListGames(authToken);
            int i = 1;
                for (ListGamesHelperResult game : result.games()) {
                    gameHashMap.put(String.valueOf(i), game);
                    string.append(String.format("%d. White Username: %s | Black Username: %s | Game Name: %s\n",
                            i, game.whiteUsername(), game.blackUsername(), game.gameName()));
                    i += 1;
                }
            if (!string.isEmpty()) {
                return string.toString();
            } else {
                return "No games in database";
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
        }
    }

    public String joinGame(String... params) throws ResponseException {
        ListGamesHelperResult game = gameHashMap.get(params[0]);
        if (params.length >= 2) {
            try {
                server.facadeJoinGame(authToken, params[1].toUpperCase(), game.gameID());
                state = State.GAMEPLAY;
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                ManifestBoard manifestBoard = new ManifestBoard(board, params[1]);
                manifestBoard.run();
            } catch (Exception e) {
                throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: Expected <gameID> [WHITE | BLACK]\u001B[0m");
    }

    public String observe(String... params) throws ResponseException {
        state = State.GAMEPLAY;
        if (params.length >= 1) {
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            ManifestBoard manifestBoard = new ManifestBoard(board, "WHITE");
            try {
                manifestBoard.run();
                return "";
            } catch (Exception e) {
                throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: Expected <gameID>\u001B[0m");
        }
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    \u001B[33mregister <USERNAME> <PASSWORD> <EMAIL>\u001B[0m - to create an account
                    \u001B[34mlogin <USERNAME> <PASSWORD>\u001B[0m - to play chess
                    \u001B[33mquit\u001B[0m - to exit the program
                    \u001B[34mhelp\u001B[0m - for possible commands
                    """;
        } else if (state == State.SIGNEDIN){
            return """
                    \u001B[33mlogout\u001B[0m - when you are done
                    \u001B[34mcreate <GAMENAME>\u001B[0m - to create a game
                    \u001B[33mlist\u001B[0m - to list all games
                    \u001B[34mjoin <ID> [WHITE|BLACK]\u001B[0m - to join an existing game
                    \u001B[33mobserve <ID>\u001B[0m - to observe an active game
                    """;
        } else {
            return """
                    \u001B[33mhelp\u001B[0m - for possible commands
                    \u001B[34mquit\u001B[0m - to quit
                    \u001B[33mreturn\u001B[0m - to go back to menu
                    """;
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mYou must sign in");
        }
    }

    private String returnToLogin() {
        state = State.SIGNEDIN;
        return "You are no longer playing\n" + help();
    }
}
