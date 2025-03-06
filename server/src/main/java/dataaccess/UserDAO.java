package dataaccess;

import model.UserData;

public interface UserDAO extends DataDAO {
    public void createUser(UserData newUser);

    public UserData getUser(String username);

    public void clear();

    public boolean isEmpty();
}
