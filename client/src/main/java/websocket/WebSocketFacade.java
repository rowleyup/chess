package websocket;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler handler;

    public WebSocketFacade(String url, NotificationHandler handler) {}

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
