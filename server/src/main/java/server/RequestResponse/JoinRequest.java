package server.RequestResponse;

public record JoinRequest(chess.ChessGame.TeamColor playerColor, int gameID) {}
