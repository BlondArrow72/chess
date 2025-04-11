package serverfacade;

import requests.RegisterRequest;
import requests.LoginRequest;
import requests.CreateGameRequest;
import requests.ListGamesRequest;
import requests.JoinGameRequest;

import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.LoginResponse;
import responses.RegisterResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class ServerFacade {
    String serverUrl;

    public ServerFacade(int desiredPort) {
        String baseURL = "http://localhost:";
        serverUrl = baseURL + desiredPort;
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException {
        String path = "/user";
        return makeRequest("POST", path, registerRequest, RegisterResponse.class, null);
    }

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException {
        String path = "/session";
        return makeRequest("POST", path, loginRequest, LoginResponse.class, null);
    }

    public void logout(String authToken) {
        String path = "/session";
        makeRequest("DELETE", path, authToken, null, authToken);
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) {
        String path = "/game";
        return makeRequest("POST", path, createGameRequest, CreateGameResponse.class, createGameRequest.authToken());
    }

    public ListGamesResponse listGames(ListGamesRequest listGamesRequest) {
        String path = "/game";
        return makeRequest("GET", path, null, ListGamesResponse.class, listGamesRequest.authToken());
    }

    public void joinGame(JoinGameRequest joinGameRequest) {
        String path = "/game";
        makeRequest("PUT", path, joinGameRequest, null, joinGameRequest.authToken());
    }

    public void clear() {
        String path = "/db";
        makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Type responseType, String authToken) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }

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
}
