package dataaccess;

import chess.ChessGame;

import model.GameData;

import responses.ListGameResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        String[] createGameTableStatements = {
                """
                CREATE TABLE IF NOT EXISTS games (
                    gameID int NOT NULL AUTO_INCREMENT,
                    whiteUsername varchar(255),
                    blackUsername varchar(255),
                    gameName varchar(255) NOT NULL,
                    chessGame TEXT,
                    PRIMARY KEY (gameID)
                )
                """
        };

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createGameTableStatements) {
                try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public int createGame(String gameName) throws DataAccessException {
        String createGameStatement = "INSERT INTO games (gameName, chessGame) VALUES (?, ?)";
        ChessGame chessGame = new ChessGame();
        String chessGameJson = new Gson().toJson(chessGame);

        try (Connection conn = DatabaseManager.getConnection()){
            try (PreparedStatement preparedStatement = conn.prepareStatement(createGameStatement, PreparedStatement.RETURN_GENERATED_KEYS)) {
                // insert gameName
                preparedStatement.setString(1, gameName);

                // insert chessGameJson
                preparedStatement.setString(2, chessGameJson);

                // executeUpdate
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataAccessException("Failed to create game.");
                }

                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                else {
                    throw new DataAccessException("Unable to retrieve gameID.");
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        String getGameStatement = "SELECT * FROM games WHERE gameID=?";

        try (Connection conn = DatabaseManager.getConnection()){
            try (PreparedStatement preparedStatement = conn.prepareStatement(getGameStatement)) {
                preparedStatement.setInt(1, gameID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String whiteUsername = resultSet.getString("whiteUsername");
                        String blackUsername = resultSet.getString("blackUsername");
                        String gameName = resultSet.getString("gameName");
                        String chessGameJson = resultSet.getString("chessGame");
                        ChessGame chessGame = new Gson().fromJson(chessGameJson, ChessGame.class);

                        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                    }
                    else {
                        throw new DataAccessException("Unable to get game.");
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public Collection<ListGameResponse> listGames() throws DataAccessException {
        String listGamesStatement = "SELECT gameID, whiteUsername, blackUsername, gameName FROM games";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(listGamesStatement)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    Collection<ListGameResponse> gameList = new ArrayList<>();

                    while (resultSet.next()) {
                        // extract data from each row
                        int gameID = resultSet.getInt("gameID");
                        String whiteUsername = resultSet.getString("whiteUsername");
                        String blackUsername = resultSet.getString("blackUsername");
                        String gameName = resultSet.getString("gameName");

                        // put into listGameResponse
                        ListGameResponse listGameResponse = new ListGameResponse(gameID, whiteUsername, blackUsername, gameName);

                        // add to collection
                        gameList.add(listGameResponse);
                    }

                    return gameList;
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        String updateGameString = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, chessGame=? WHERE gameID=?";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(updateGameString)) {
                preparedStatement.setString(1, whiteUsername);
                preparedStatement.setString(2, blackUsername);
                preparedStatement.setString(3, gameName);

                String chessGameJson = new Gson().toJson(game);
                preparedStatement.setString(4, chessGameJson);

                preparedStatement.setInt(5, gameID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataAccessException("Unable to update game.");
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        String clearStatement = "TRUNCATE games";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(clearStatement)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isEmpty() throws DataAccessException {
        String isEmptyStatement = "SELECT COUNT(*) FROM games";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(isEmptyStatement)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int numGames = resultSet.getInt(1);

                        return numGames == 0;
                    }
                    else {
                        throw new DataAccessException("Unable to retrieve number of games.");
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
