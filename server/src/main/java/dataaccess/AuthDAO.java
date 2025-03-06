package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData newAuth);

    public AuthData getAuth(String authToken);

    public void deleteAuth(String authToken);

    public void clear();
}
