package dataaccess;

import chess.ChessGame;
import io.javalin.http.HttpResponseException;
import model.GameData;
import service.GameService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GameSqlDAO {
    private Connection connection;

    public GameSqlDAO() throws Exception {
        configureDatabase();
    }

    private final String[] createGameTable = {
            """
    CREATE TABLE IF NOT EXISTS games (
    'gameID' INT NOT NULL AUTO_INCREMENT,
    'whiteUsername' VARCHAR(256) NOT NULL,
    'blackUsername' VARCHAR(256) NOT NULL,
    'gameName' VARCHAR(256) NOT NULL,
    'game' CHESS GAME NOT NULL,
    PRIMARY KEY (gameID) )
"""
    };

    public void createGame(String gameName) throws Exception {
        String sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, null);
            stmt.setString(2, null);
            stmt.setString(3, gameName);
            stmt.setString(4, null);
            stmt.executeUpdate();
        }
    }

    public Integer currentGameID() throws Exception{
        String sql = "SELECT MAX(gameID) FROM games";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    public GameData getGame(Integer gameID) throws Exception{
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            return new GameData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4, ))
        }
    }


    private void configureDatabase() throws HttpResponseException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createGameTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException e) {

        }
    }
}
