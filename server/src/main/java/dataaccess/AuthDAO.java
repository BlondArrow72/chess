package dataaccess;

import model.AuthData;

public interface AuthDAO extends DataDAO{
    public AuthData createAuth(String username) throws DataAccessException;

    public AuthData getAuth(String authToken) throws DataAccessException;

    public void deleteAuth(String authToken) throws DataAccessException;

    public void clear() throws DataAccessException;

    public boolean isEmpty() throws DataAccessException;
}
