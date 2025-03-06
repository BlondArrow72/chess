package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private final HashMap<String, UserData> userDataDatabase;

    public MemoryUserDAO() {
        userDataDatabase = new HashMap<>();
    }

    public void createUser(UserData newUser) {
        userDataDatabase.put(newUser.username(), newUser);
    }

    public UserData getUser(String username) {
        return userDataDatabase.get(username);
    }

    public void clear() {
        userDataDatabase.clear();
    }

    public boolean isEmpty() {
        return userDataDatabase.isEmpty();
    }
}
