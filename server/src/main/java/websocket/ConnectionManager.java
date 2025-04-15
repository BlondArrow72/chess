package websocket;

import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        Connection connection = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void sendAll(LoadGameMessage loadGameMessage) throws IOException {
        String loadGameMessageJson = new Gson().toJson(loadGameMessage);
        for (Connection connection: connections.values()) {
            connection.send(loadGameMessageJson);
        }

        cleanUp();
    }

    public void broadcast(String excludeUsername, NotificationMessage notificationMessage) throws IOException {
        String notificationJson = new Gson().toJson(notificationMessage);
        for (Connection connection: connections.values()) {
            if (!connection.username.equals(excludeUsername)) {
                connection.send(notificationJson);
            }
        }

        cleanUp();
    }

    public void reply(String username, ServerMessage serverMessage) throws IOException {
        String serverMessageJson = new Gson().toJson(serverMessage);
        connections.get(username).send(serverMessageJson);

        cleanUp();
    }

    public void notifyAll(ServerMessage serverMessage) throws IOException {
        String serverMessageJson = new Gson().toJson(serverMessage);
        for (Connection connection: connections.values()) {
            connection.send(serverMessageJson);
        }

        cleanUp();
    }

    private void cleanUp() {
        for (Connection connection: connections.values()) {
            if (!connection.session.isOpen()) {
                remove(connection.username);
            }
        }
    }
}
