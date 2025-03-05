package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import handlers.RegisterRequest;
import handlers.RegisterResponse;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final MemoryUserDAO userDAO = new MemoryUserDAO();
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public RegisterResponse register(RegisterRequest registerRequest) throws DataAccessException {
        // unpack values
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();

        // createUser
        if (userDAO.getUser(username) == null) {
            userDAO.createUser(username, password, email);
        }
        else {
            throw new DataAccessException("Username already taken.");
        }

        // createAuth
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(authToken, username);

        // return RegisterResult
        return new RegisterResponse(200, List.of(username, authToken));
    }

    // public LoginResult login(LoginRequest loginRequest) {}

    // public void logout(LogoutRequest logoutRequest) {}
}
