package request;

import com.google.gson.annotations.Expose;

public record JoinRequest(@Expose chess.ChessGame.TeamColor playerColor, @Expose int gameID) {}
