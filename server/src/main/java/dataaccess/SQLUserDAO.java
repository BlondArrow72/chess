package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    public void createSQLUserDAO() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String[] preparedStatements = {
                    """
                    CREATE TABLE IF NOT EXISTS users (
                        'username' varchar(255) NOT NULL,
                        'password' int NOT NULL,
                        'email' varchar(255),
                        PRIMARY KEY ('username')
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """
            };

            for (String statement : preparedStatements) {
                try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }

        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void createUser(UserData newUser);

    public UserData getUser(String username);

    public void clear();

    public boolean isEmpty();
}
