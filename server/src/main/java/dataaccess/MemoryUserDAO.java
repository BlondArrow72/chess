package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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

        String hashedPassword = BCrypt.hashpw(newUser.password(), BCrypt.gensalt());
        UserData user = new UserData(newUser.username(), hashedPassword, newUser.email());

        userDataDatabase.put(newUser.username(), user);
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
