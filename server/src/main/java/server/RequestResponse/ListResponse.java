package server.RequestResponse;

import com.google.gson.annotations.Expose;
import model.GameData;
import java.util.Collection;

public record ListResponse(@Expose Collection<GameData> games) {}
