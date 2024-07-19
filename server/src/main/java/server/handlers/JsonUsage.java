package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collection;

public class JsonUsage {
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static <T> T fromJson(String json, Class<T> c) {
        return new Gson().fromJson(json, c);
    }

    public static String getJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> String getJson(Collection<T> coll) {
        return gson.toJson(coll);
    }

    public static String fromError(String message) {
        return String.format("{ \"message\": \"%s\" }", message);
    }
}
