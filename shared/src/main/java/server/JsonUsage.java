package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;

/**
 * Class for holding Gson data and implementing json-related functions
 */
public class JsonUsage {
    private static final Gson GSON = new GsonBuilder().create();

    /**
     * Translates the given json string into the given object
     *
     * @param json is a json string
     * @param c is the class of the object
     * @return an object of the given type
     * @param <T> the type of object
     */
    public static <T> T fromJson(String json, Class<T> c) {
        return new Gson().fromJson(json, c);
    }

    public static <T> T fromJson(InputStreamReader reader, Class<T> c) {
        return new Gson().fromJson(reader, c);
    }

    /**
     * Translates the given object into a json string
     *
     * @param obj an object to be translated
     * @return a json string representing the object
     */
    public static String getJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * Translates an error message into a json string
     *
     * @param message is a string containing the thrown error message
     * @return a json string containing the error
     */
    public static String fromError(String message) {
        return String.format("{ \"message\": \"%s\" }", message);
    }

    public static String toError(String message) {
        String m = message.substring(13);
        m = m.substring(0, m.length() - 2);
        return m;
    }
}
