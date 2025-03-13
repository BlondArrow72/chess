package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private final HashMap<String, AuthData> authDataDatabase;

    public MemoryAuthDAO() {
        authDataDatabase = new HashMap<>();
    }

    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authDataDatabase.put(newAuth.authToken(), newAuth);
        return newAuth;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return authDataDatabase.get(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        authDataDatabase.remove(authToken);
    }

    public void clear() throws DataAccessException {
        authDataDatabase.clear();
    }

    public boolean isEmpty() throws DataAccessException {
        return authDataDatabase.isEmpty();
    }
}
