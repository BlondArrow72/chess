package dataaccess;

import java.util.HashMap;

public class UserDataAccess {
    private HashMap<String, UserData> userDataDatabase = new HashMap<>();

    public void addUserData(String username, String password, String email) {
        UserData newUserData = new UserData(username, password, email);
        userDataDatabase.put(username, newUserData);
    }

    public UserData getUserData(String username) {
        return userDataDatabase.get(username);
    }

    public void deleteUserData(String username) {
        userDataDatabase.remove(username);
    }

    public void deleteAllUserData() {
        userDataDatabase.clear();
    }
}
