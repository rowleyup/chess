package ui;

import model.AuthData;
import server.ResponseException;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class InGameClient {
    private final AuthData userAuth;
    private int gameId;
    private final WebSocketFacade ws;

    public InGameClient(AuthData auth, NotificationHandler notificationHandler, String url) {
        this.userAuth = auth;
        try {
            ws = new WebSocketFacade(url, notificationHandler);
        } catch (ResponseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void setId(int id) {
        gameId = id;
    }

    public String help() {
        String message = SET_TEXT_COLOR_BLUE + "\n\tredraw " + SET_TEXT_COLOR_LIGHT_GREY + "- redraw chess board\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tmove " + SET_TEXT_COLOR_LIGHT_GREY + "- move a chess piece\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thighlight " + SET_TEXT_COLOR_LIGHT_GREY + "- highlight legal moves for a piece\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tresign " + SET_TEXT_COLOR_LIGHT_GREY + "- resign from the game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tleave " + SET_TEXT_COLOR_LIGHT_GREY + "- leave game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thelp " + SET_TEXT_COLOR_LIGHT_GREY + "- display this message again\n";
        return message;
    }

    public String overHelp() {
        String message = SET_TEXT_COLOR_BLUE + "\n\tredraw " + SET_TEXT_COLOR_LIGHT_GREY + "- redraw chess board\n";
        message = message + SET_TEXT_COLOR_LIGHT_GREY + "\tmove - move a chess piece\n";
        message = message + SET_TEXT_COLOR_LIGHT_GREY + "\thighlight - highlight legal moves for a piece\n";
        message = message + SET_TEXT_COLOR_LIGHT_GREY + "\tresign - resign from the game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tleave " + SET_TEXT_COLOR_LIGHT_GREY + "- leave game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thelp " + SET_TEXT_COLOR_LIGHT_GREY + "- display this message again\n";
        return message;
    }

    public String observeHelp() {
        String message = SET_TEXT_COLOR_BLUE + "\n\tredraw " + SET_TEXT_COLOR_LIGHT_GREY + "- redraw chess board\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thighlight - highlight legal moves for a piece\n";
        message = message + SET_TEXT_COLOR_LIGHT_GREY + "\tmove - move a chess piece\n";
        message = message + SET_TEXT_COLOR_LIGHT_GREY + "\tresign - resign from the game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tleave " + SET_TEXT_COLOR_LIGHT_GREY + "- leave game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thelp " + SET_TEXT_COLOR_LIGHT_GREY + "- display this message again\n";
        return message;
    }

    public void connect() throws ResponseException {
        ws.connect(userAuth, gameId);
    }

    public String leave() throws ResponseException {
        ws.leave(userAuth, gameId);
        return "Leaving game";
    }

    public void move(chess.ChessPosition from, chess.ChessPosition to, chess.ChessPiece.PieceType promote) throws ResponseException {
        ws.move(userAuth, gameId, new chess.ChessMove(from, to, promote));
    }

    public String resign() throws ResponseException {
        ws.resign(userAuth, gameId);
        return "GAME OVER: You have resigned";
    }
}
