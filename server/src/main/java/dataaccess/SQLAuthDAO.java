package dataaccess;

import model.AuthData;
import service.UnauthorizedUserError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String[] createAuthTableStatements = {
                    """
                    CREATE TABLE IF NOT EXISTS auths (
                        authToken varchar(255) NOT NULL,
                        username varchar(255) NOT NULL,
                        PRIMARY KEY (authToken)
                    )
                    """
            };

            for (String statement : createAuthTableStatements) {
                try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData createAuth(String username) throws DataAccessException {
        String createAuthStatement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        String authToken = UUID.randomUUID().toString();

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(createAuthStatement)) {
                // insert authToken
                preparedStatement.setString(1, authToken);

                // insert username
                preparedStatement.setString(2, username);

                // execute update
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataAccessException("Failed to create auth.");
                }

                return new AuthData(authToken, username);
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        String getAuthStatement = "SELECT authToken, username FROM auths WHERE authToken=?";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(getAuthStatement)) {
                preparedStatement.setString(1, authToken);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String username = resultSet.getString("username");
                        return new AuthData(authToken, username);
                    }
                    else {
                        throw new UnauthorizedUserError();
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        String deleteAuthString = "DELETE FROM auths WHERE authToken=?";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(deleteAuthString)) {
                preparedStatement.setString(1, authToken);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataAccessException("Unable to delete auth.");
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String clearStatement = "TRUNCATE auths";

            try (PreparedStatement preparedStatement = conn.prepareStatement(clearStatement)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isEmpty() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String isEmptyStatement = "SELECT COUNT(*) FROM auths";

            try (PreparedStatement preparedStatement = conn.prepareStatement(isEmptyStatement)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int numAuths = resultSet.getInt(1);

                        return numAuths == 0;
                    }
                    else {
                        throw new DataAccessException("Unable to retrieve number of auths.");
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
