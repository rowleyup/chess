package server;

import model.*;
import request.ListResponse;
import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String url;

    public ServerFacade(String url) {
        this.url = url;
    }

    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return makeRequest("POST", path, null, user, AuthData.class);
    }

    public AuthData login(UserData user) throws ResponseException {
        var path = "/session";
        return makeRequest("POST", path, null, user, AuthData.class);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        makeRequest("DELETE", path, authToken, null, null);
    }

    public ListResponse listGames(String authToken) throws ResponseException {
        var path = "/game";
        return makeRequest("GET", path, authToken, null, ListResponse.class);
    }

    public GameData createGame(String authToken, GameData game) throws ResponseException {
        var path = "/game";
        return makeRequest("POST", path, authToken, game, GameData.class);
    }

    public void joinGame(String authToken, GameData game) throws ResponseException {
        var path = "/game";
        makeRequest("PUT", path, authToken, game, null);
    }

    private <T> T makeRequest(String method, String path, String header, Object request, Class<T> resType) throws ResponseException {
        try {
            URL u = (new URI(url + path)).toURL();
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            writeBody(request, conn);

            if (header != null) {
                writeHeader(header, conn);
            }

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

    private void writeHeader(String header, HttpURLConnection http) {
        if (header != null) {
            http.addRequestProperty("authorization", header);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection conn) throws IOException, ResponseException {
        var status = conn.getResponseCode();
        if (status != 200) {
            String message = JsonUsage.toError(readBody(conn, String.class));
            throw new ResponseException(String.format("Failure: %s - %s", status, message));
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
