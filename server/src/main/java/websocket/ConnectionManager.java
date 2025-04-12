package websocket;

import websocket.messages.NotificationMessage;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        Connection connection = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void notify(String excludeUsername, NotificationMessage notificationMessage) throws IOException {
        String notificationJson = new Gson().toJson(notificationMessage);
        for (Connection connection: connections.values()) {
            if (!connection.username.equals(excludeUsername)) {
                connection.send(notificationJson);
            }
        }
    }
}
