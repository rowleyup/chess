package server.handlers;

import com.google.gson.Gson;

public class JsonUsage {
    public static <T> T fromJson(String json, Class<T> c) {
        return new Gson().fromJson(json, c);
    }

    public static String getJson(Object obj) {
        return new Gson().toJson(obj);
    }

    public static String fromError(String message) {
        return String.format("{ \"message\": \"%s\" }", message);
    }
}
