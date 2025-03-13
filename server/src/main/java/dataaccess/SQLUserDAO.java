package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String[] createUserTablestatements = {
                    """
                    CREATE TABLE IF NOT EXISTS users (
                        'username' varchar(255) NOT NULL,
                        'password' int NOT NULL,
                        'email' varchar(255),
                        PRIMARY KEY ('username')
                    )
                    """
            };

            for (String statement : createUserTablestatements) {
                try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void createUser(UserData newUser) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            // prepare create statement
            String createUserStatement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(createUserStatement)) {
                // put username in the right spot
                preparedStatement.setString(1, newUser.username());

                // encrypt password and put in the right spot
                String hashedPassword = BCrypt.hashpw(newUser.password(), BCrypt.gensalt());
                preparedStatement.setString(2, hashedPassword);

                // put email in the right spot
                preparedStatement.setString(3, newUser.email());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataAccessException("Failed to create user.");
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            // prepare get statement
            String getUserStatement = "SELECT username FROM users WHERE username=?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(getUserStatement)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String password = resultSet.getString("password");
                        String email = resultSet.getString("email");

                        return new UserData(username, password, email);
                    }
                    else {
                        throw new DataAccessException("User does not exist.");
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String clearStatement = "TRUNCATE users";

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
            String isEmptyStatement = "SELECT COUNT(*) FROM users";

            try (PreparedStatement preparedStatement = conn.prepareStatement(isEmptyStatement)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int numUsers = resultSet.getInt(1);

                        return numUsers == 0;
                    }
                    else {
                        throw new DataAccessException("Unable to retrieve number of users.");
                    }
                }


            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
