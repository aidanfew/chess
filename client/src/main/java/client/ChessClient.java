package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import results.*;
import server.ServerFacade;
import ui.ManifestBoard;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.sql.Array;
import java.util.*;

public class ChessClient implements ServerMessageHandler {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken;
    private final HashMap<String, ListGamesHelperResult> gameHashMap = new HashMap<>();
    private final WebSocketFacade ws;
    private String perspectiveColor = "WHITE";

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
        ws = new WebSocketFacade(serverUrl, this);
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
                    case "leave" -> leave();
                    case "move" -> makeMove(params);
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
            if (gameHashMap.containsKey(params[0])) {
                try {
                    server.facadeJoinGame(authToken, params[1].toUpperCase(), game.gameID());
                    ws.webSocketFacadeConnect(authToken, params[0]);
                    perspectiveColor = params[1];
                    state = State.GAMEPLAY;
//                    ChessBoard board = new ChessBoard();
//                    board.resetBoard();
//                    ManifestBoard manifestBoard = new ManifestBoard(board, params[1]);
//                    manifestBoard.run();
                } catch (Exception e) {
                    throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
                }
            } else {
                return "\u001B[31mError: Invalid GameID\u001B[0m";
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: Expected <gameID> [WHITE | BLACK]\u001B[0m");
        }
        return "";
    }

    public String observe(String... params) throws ResponseException {
        state = State.GAMEPLAY;
        if (params.length >= 1) {
//            ChessBoard board = new ChessBoard();
//            board.resetBoard();
//            ManifestBoard manifestBoard = new ManifestBoard(board, "WHITE");
            try {
                if (gameHashMap.containsKey(params[0])) {
                    perspectiveColor = "WHITE";
                    ws.webSocketFacadeConnect(authToken, params[0]);
//                    manifestBoard.run();
                    return "";
                } else {
                    return "\u001B[31mError: Invalid GameID\u001B[0m";
                }
            } catch (Exception e) {
                throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: Expected <gameID>\u001B[0m");
        }
    }

    private String leave() throws ResponseException {
        assertInGameplay();
        try {
            ws.webSocketFacadeLeave(authToken);
            state = State.SIGNEDIN;
            return "You are no longer playing\n" + help();
        } catch (Exception e) {
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }

    private String makeMove(String... params) throws ResponseException {
        assertInGameplay();
        if (params.length >= 2) {
            try {
                ChessMove move = convertMove(params[0], params[1]);
                ws.webSocketFacadeMakeMove(authToken, move);
            } catch (Exception e) {
                throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
            }
        }
    }

    private ChessMove convertMove(String start, String end) throws ResponseException {
        char startFile = start.charAt(0);
        char startRank = start.charAt(1);
        char endFile = end.charAt(0);
        char endRank = end.charAt(1);
        if (rankVerified(startRank) && rankVerified(endRank)) {
            int newStartFile = fileToColumn(startFile);
            int newEndFile = fileToColumn(endFile);
            if (Objects.equals(newStartFile, 0) || Objects.equals(newEndFile, 0)) {
                throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: invalid move\u001B[0m");
            } else {
                return new ChessMove(new ChessPosition(startRank, newStartFile), new ChessPosition(endRank, newEndFile), null);
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: invalid move\u001B[0m");
        }

    }

    private int fileToColumn(char file) {
        return switch (file) {
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            default -> 0;
        };
    }

    private boolean rankVerified(char rank) {
        ArrayList<String> list = new ArrayList<>(List.of("1", "2", "3", "4", "5", "6", "7", "8"));
        return list.contains(String.valueOf(rank));
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
                    \u001B[34mredraw\u001B[0m - to redraw the board
                    \u001B[33mleave\u001B[0m - to leave the game
                    \u001B[34mmove <START> <END>\u001B[0m - to make a move
                    \u001B[33mresign\u001B[0m - to forfeit the game
                    \u001B[34mhighlight\u001B[0m - your legal moves
                    \u001B[33mquit\u001B[0m - to exit the system
                    """;
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: You must sign in");
        }
    }

    private void assertInGameplay() throws ResponseException {
        if (state != State.GAMEPLAY) {
            throw new ResponseException(ResponseException.Code.ClientError, "\u001B[31mError: You are not in a game");
        }
    }

    @Override
    public void notifyLoadGame(LoadGameMessage serverMessage) {
        System.out.println("Load Game received");
        ChessGame game = serverMessage.giveGame();
        ManifestBoard manifestBoard = new ManifestBoard(game.getBoard(), perspectiveColor);
        manifestBoard.run();
    }

    @Override
    public void notifyError(ErrorMessage serverMessage) {
        System.out.println(serverMessage);
    }

    @Override
    public void notifyNotification(NotificationMessage serverMessage) {
        System.out.println(serverMessage);
    }
}
