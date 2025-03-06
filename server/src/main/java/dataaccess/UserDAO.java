package dataaccess;

import model.UserData;

public interface UserDAO {
    public void createUser(UserData newUser);

    public UserData getUser(String username);

    public void clear();
}
