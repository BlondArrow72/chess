package serverFacade;

import handlers.CreateGameRequest;
import handlers.JoinGameRequest;

import model.AuthData;

import handlers.LoginRequest;
import handlers.ListGamesResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

import model.UserData;
import org.eclipse.jetty.client.HttpResponseException;

import com.google.gson.Gson;

public class ServerFacade {
    public AuthData register(UserData newUser) throws ResponseException {
        String path = "/user";
        return makeRequest("POST", path, newUser, AuthData.class);
    }

    public AuthData login(LoginRequest loginRequest) throws ResponseException {
        String path = "/session";
        return makeRequest("POST", path, loginRequest, AuthData.class);
    }

    public void logout(AuthData userAuth) {
        String path = "/session";
        makeRequest("DELETE", path, userAuth, null);
    }

    public int createGame(CreateGameRequest createGameRequest) {
        String path = "/game";
        return makeRequest("POST", path, createGameRequest, Integer.class);
    }

    public Collection<ListGamesResponse> listGames(String authToken) {
        String path = "/game";
        return makeRequest("GET", path, authToken, ListGamesResponse.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) {
        String path = "/game";
        makeRequest("PUT", path, joinGameRequest, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws HttpResponseException {
        try {
            String serverUrl = "http://localhost:8080";
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        }
        catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        int status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }

        return response;
    }

    private boolean isSuccessful(int status) {
        return (status / 100 == 2);
    }
}
