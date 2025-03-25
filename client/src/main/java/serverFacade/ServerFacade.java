package serverFacade;

import model.UserData;
import model.AuthData;
import model.CreateGameRequest;
import model.JoinGameRequest;
import model.LoginRequest;
import model.ListGamesResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.util.Collection;
import java.util.List;

import java.lang.reflect.Type;

import org.eclipse.jetty.client.HttpResponseException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ServerFacade {
    String serverUrl;

    public ServerFacade(int desiredPort) {
        String baseURL = "http://localhost:";
        serverUrl = baseURL + desiredPort;
    }

    public AuthData register(UserData newUser) throws ResponseException {
        String path = "/user";
        return makeRequest("POST", path, newUser, AuthData.class);
    }

    public AuthData login(LoginRequest loginRequest) throws ResponseException {
        String path = "/session";
        return makeRequest("POST", path, loginRequest, AuthData.class);
    }

    public void logout(String authToken) {
        String path = "/session";
        makeRequest("DELETE", path, authToken, null);
    }

    public void createGame(CreateGameRequest createGameRequest) {
        String path = "/game";
        makeRequest("POST", path, createGameRequest, Integer.class);
    }

    public Collection<ListGamesResponse> listGames(String authToken) {
        String path = "/game";
        Type collectionType = new TypeToken<List<ListGamesResponse>>() {}.getType();
        return makeRequest("GET", path, authToken, collectionType);
    }

    public void joinGame(JoinGameRequest joinGameRequest) {
        String path = "/game";
        makeRequest("PUT", path, joinGameRequest, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Type responseType) throws HttpResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseType);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
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
        if (status == 400) {
            throw new ResponseException(status, "Error: bad request");
        }
        else if (status == 401) {
            throw new ResponseException(status, "Error: unauthorized");
        }
        else if (status == 403) {
            throw new ResponseException(status, "Error: already taken");
        }
        else if (status == 500) {
            throw new ResponseException(status, "Database error.");
        }
    }

    private static <T> T readBody(HttpURLConnection http, Type responseType) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseType != null) {
                    response = new Gson().fromJson(reader, responseType);
                }
            }
        }

        return response;
    }

    private boolean isSuccessful(int status) {
        return (status / 100 == 2);
    }
}
