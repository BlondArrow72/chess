package websocket;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

import java.io.IOException;

public class Connection {
    public final Session session;
    public final String username;

    public Connection(String username, Session session) {
        this.username = username;
        this.session = session;
    }

    public void send(String message) throws IOException {
        if (session.isOpen()) {
            session.getRemote().sendString(message);
        }
    }
}