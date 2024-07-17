package dataaccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private HashMap<String, String[]> users;

    public MemoryUserDAO() {
        users = new HashMap<>();
    }

    public UserData getUser(String username) {
        String[] passMail = users.get(username);
        if (passMail == null) {
            return null;
        }
        return new UserData(username, passMail[0], passMail[1]);
    }

    public boolean createUser(UserData user) {
        String username = user.username();
        if (users.containsKey(username)) {
            return false;
        }

        String[] passMail = new String[2];
        passMail[0] = user.password();
        passMail[1] = user.email();
        users.put(username, passMail);
        return true;
    }

    public boolean clearUsers() {
        users = new HashMap<>();
        return true;
    }
}
