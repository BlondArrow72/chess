package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private final HashMap<String, UserData> userDataDatabase;

    public MemoryUserDAO() {
        userDataDatabase = new HashMap<>();
    }

    public void createUser(UserData newUser) throws DataAccessException {
        if (userDataDatabase.get(newUser.username()) != null) {
            throw new DataAccessException("User already exists");
        }

        userDataDatabase.put(newUser.username(), newUser);
    }

    public UserData getUser(String username) throws DataAccessException {
        return userDataDatabase.get(username);
    }

    public void clear() throws DataAccessException {
        userDataDatabase.clear();
    }

    public boolean isEmpty() throws DataAccessException {
        return userDataDatabase.isEmpty();
    }
}
