package server.websocket;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<String, Connection>();

    public void join() {}

    public void observe() {}

    public void leave() {}
}
