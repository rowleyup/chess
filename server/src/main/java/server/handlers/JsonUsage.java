package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class for holding Gson data and implementing json-related functions
 */
public class JsonUsage {
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

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

    /**
     * Translates the given object into a json string
     *
     * @param obj an object to be translated
     * @return a json string representing the object
     */
    public static String getJson(Object obj) {
        return gson.toJson(obj);
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
}
