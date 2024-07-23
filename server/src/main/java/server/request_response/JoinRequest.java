package server.request_response;

public record JoinRequest(chess.ChessGame.TeamColor playerColor, int gameID) {}
