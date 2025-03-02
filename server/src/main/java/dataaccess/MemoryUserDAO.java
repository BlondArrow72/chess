package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private final HashMap<String, UserData> userDataDatabase = new HashMap<>();

    public void createUser(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        userDataDatabase.put(username, newUser);
    }

    public UserData getUser(String username) {
        return userDataDatabase.get(username);
    }

    public void clear() {
        userDataDatabase.clear();
    }
}
