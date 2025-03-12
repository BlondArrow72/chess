package dataaccess;

import model.UserData;

public interface UserDAO extends DataDAO {
    public void createUser(UserData newUser) throws DataAccessException;

    public UserData getUser(String username) throws DataAccessException;

    public void clear() throws DataAccessException;

    public boolean isEmpty() throws DataAccessException;
}
