package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUsage {
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static <T> T fromJson(String json, Class<T> c) {
        return new Gson().fromJson(json, c);
    }

    public static String getJson(Object obj) {
        return gson.toJson(obj);
    }

    public static String fromError(String message) {
        return String.format("{ \"message\": \"%s\" }", message);
    }
}
