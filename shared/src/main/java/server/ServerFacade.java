package server;

import model.*;
import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String url;

    public ServerFacade(String url) {
        this.url = url;
    }

    public void register(UserData user) {}

    public void login(UserData user) {}

    public void logout(String authToken) {}

    public void listGames(String authToken) {}

    public void createGame(String authToken, GameData game) {}

    public void joinGame(String authToken, GameData game) {}

    private <T> T makeRequest(String method, String path, Object request, Class<T> resType) throws ResponseException {
        try {
            URL u = (new URI(url + path)).toURL();
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            writeBody(request, conn);
            conn.connect();
            throwIfNotSuccessful(conn);

            return readBody(conn, resType);
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String data = JsonUsage.getJson(request);
            try (OutputStream os = http.getOutputStream()) {
                os.write(data.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection conn) throws IOException, ResponseException {
        var status = conn.getResponseCode();
        if (status != 200) {
            throw new ResponseException(String.format("Failure: %s", status));
        }
    }

    private <T> T readBody(HttpURLConnection conn, Class<T> resType) throws IOException {
        T res = null;
        if (conn.getContentLength() < 0) {
            try (InputStream resBody = conn.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(resBody);
                if (resType != null) {
                    res = JsonUsage.fromJson(reader, resType);
                }
            }
        }
        return res;
    }
}
