package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;

public class Connection {
    public String username;
    public Session session;
    public SessionRole role;
    public boolean isOver;

    public enum SessionRole {
        WHITE,
        BLACK,
        OBSERVER
    }

    public Connection(String username, Session session, SessionRole role) {
        this.username = username;
        this.session = session;
        this.role = role;
        this.isOver = false;
    }

    public void send(String message) throws IOException {
        session.getRemote().sendString(message);
    }
}
