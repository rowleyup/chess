import server.Server;

public class Main {
    public static void main(String[] args) {
        try {
            int port = 8080;
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            port = new Server().run(port);
            System.out.printf("Server started on port %s%n", port);
        } catch (Throwable e) {
            System.out.printf("Server failed to start: %s%n", e.getMessage());
        }
    }
}
