package dataaccess;

import model.AuthData;

public interface AuthDAO extends DataDAO{
    public AuthData createAuth(String username);

    public AuthData getAuth(String authToken);

    public void deleteAuth(String authToken);

    public void clear();

    public boolean isEmpty();
}
