package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import io.javalin.http.HttpResponseException;
import model.GameData;
import results.ListGamesHelperResult;
import service.GameService;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

public class GameSqlDAO {
    private final Connection connection;

    public GameSqlDAO() throws DataAccessException {
        this.connection = DatabaseManager.getConnection();
    }

    public void createGame(String gameName) throws Exception {
        String sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, null);
            stmt.setString(2, null);
            stmt.setString(3, gameName);
            var serializer = new Gson();
            String jsonGame = serializer.toJson(new ChessGame());
            stmt.setString(4, jsonGame);
            stmt.executeUpdate();
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public Integer currentGameID() throws Exception{
        String sql = "SELECT MAX(gameID) FROM games";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public GameData getGame(Integer gameID) throws Exception{
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            String chessGame = rs.getString(5);
            var serializer = new Gson();
            ChessGame game = serializer.fromJson(chessGame, ChessGame.class);
            return new GameData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), game);
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public void replaceGame(Integer gameID, GameData oldData, GameData newData) throws Exception{
        String sql = "UPDATE games SET game=? WHERE gameID=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            var serializer = new Gson();
            String newChessGame = serializer.toJson(newData.game());
            stmt.setString(5, newChessGame);
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public Collection<ListGamesHelperResult> createHelperList() throws Exception{
        ArrayList<ListGamesHelperResult> list = new ArrayList<>();
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName FROM games";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer gameID = rs.getInt(1);
                String whiteUsername = rs.getString(2);
                String blackUsername = rs.getString(3);
                String gameName = rs.getString(4);
                list.add(new ListGamesHelperResult(gameID, whiteUsername, blackUsername, gameName));
            }
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
        return list;
    }

    public void clear() throws Exception{
        String sql = "DROP TABLE games";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }
}
