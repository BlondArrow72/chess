package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class AuthDataAccess implements DataAccessObject{
    private final HashMap<String, AuthData> authDataDatabase = new HashMap<>();

    public void createAuth(String authToken, String username) {
        AuthData newAuth = new AuthData(authToken, username);
        authDataDatabase.put(authToken, newAuth);
    }

    public AuthData getAuth(String authToken) {
        return authDataDatabase.get(authToken);
    }

    public void deleteAuth(String authToken) {
        authDataDatabase.remove(authToken);
    }

    public void clear() {
        authDataDatabase.clear();
    }
}
