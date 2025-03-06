package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private final HashMap<String, AuthData> authDataDatabase;

    public MemoryAuthDAO() {
        authDataDatabase = new HashMap<>();
    }

    public void createAuth(AuthData newAuth) {
        authDataDatabase.put(newAuth.authToken(), newAuth);
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
