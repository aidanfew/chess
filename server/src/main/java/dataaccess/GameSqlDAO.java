package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import results.ListGamesHelperResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static server.websocket.WebSocketHandler.ENDED_GAMES;

public class GameSqlDAO {

    public GameSqlDAO() {
    }

    public void createGame(String gameName) throws Exception {
        String sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, null);
                stmt.setString(2, null);
                stmt.setString(3, gameName);
                var serializer = new Gson();
                String jsonGame = serializer.toJson(new ChessGame());
                stmt.setString(4, jsonGame);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public void setUserToNull(Integer gameID, ChessGame.TeamColor color) throws Exception {
        String sql = "";
        if (Objects.equals(color, ChessGame.TeamColor.WHITE)) {
            sql = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
        } else if (Objects.equals(color, ChessGame.TeamColor.BLACK)){
            sql = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
        }
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setNull(1, 12);
                stmt.setInt(2, gameID);
                stmt.executeUpdate();
            }
        }
    }

    public Integer currentGameID() throws Exception {
        String sql = "SELECT MAX(gameID) FROM games";
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                rs.next();
                return rs.getInt(1);
            }
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public GameData getGame(Integer gameID) throws Exception {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?";
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, gameID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String chessGame = rs.getString(5);
                    var serializer = new Gson();
                    ChessGame game = serializer.fromJson(chessGame, ChessGame.class);
                    return new GameData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), game);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public void replaceGame(Integer gameID, GameData oldData, GameData newData) throws Exception{
        String sql = "UPDATE games SET whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                var serializer = new Gson();
                String newChessGame = serializer.toJson(newData.game());
                stmt.setString(1, newData.whiteUsername());
                stmt.setString(2, newData.blackUsername());
                stmt.setString(3, newChessGame);
                stmt.setInt(4, gameID);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public Collection<ListGamesHelperResult> createHelperList() throws Exception{
        ArrayList<ListGamesHelperResult> list = new ArrayList<>();
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName FROM games";
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Integer gameID = rs.getInt(1);
                    String whiteUsername = rs.getString(2);
                    String blackUsername = rs.getString(3);
                    String gameName = rs.getString(4);
                    list.add(new ListGamesHelperResult(gameID, whiteUsername, blackUsername, gameName));
                }
            }
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
        return list;
    }

    public void clear() throws Exception{
        String sql = "TRUNCATE TABLE games";
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.executeUpdate();
                ENDED_GAMES.clear();
            }
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }
}
