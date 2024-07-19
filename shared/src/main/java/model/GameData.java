package model;

import com.google.gson.annotations.Expose;

public record GameData(@Expose int gameID, @Expose String whiteUsername, @Expose String blackUsername,
                       @Expose String gameName, chess.ChessGame game) {
}
