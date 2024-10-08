package websocket;

import model.AuthData;
import server.JsonUsage;
import server.ResponseException;
import websocket.commands.*;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler handler;

    public WebSocketFacade(String url, NotificationHandler handler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.handler = handler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    handler.notify(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new ResponseException(e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connect(AuthData authToken, Integer gameId, String type) throws ResponseException {
        try {
            if (type.equals("connect")) {
                var command = new UserConnectCommand(authToken.authToken(), gameId);
                this.session.getBasicRemote().sendText(JsonUsage.getJson(command));
            }
            else if (type.equals("resign")){
                var command = new UserResignCommand(authToken.authToken(), gameId);
                this.session.getBasicRemote().sendText(JsonUsage.getJson(command));
            }
        } catch (IOException e) {
            throw new ResponseException(e.getMessage());
        }
    }

    public void move(AuthData authToken, Integer gameId, chess.ChessMove move) throws ResponseException {
        try {
            var command = new UserMoveCommand(authToken.authToken(), gameId, move);
            this.session.getBasicRemote().sendText(JsonUsage.getJson(command));
        } catch (IOException e) {
            throw new ResponseException(e.getMessage());
        }
    }

    public void leave(AuthData authToken, Integer gameId) throws ResponseException {
        try {
            var command = new UserLeaveCommand(authToken.authToken(), gameId);
            this.session.getBasicRemote().sendText(JsonUsage.getJson(command));
            this.session.close();
        } catch (IOException e) {
            throw new ResponseException(e.getMessage());
        }
    }
}
