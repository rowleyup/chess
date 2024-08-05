import ui.PreLoginRepl;
import static ui.EscapeSequences.WHITE_PAWN;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client: " + WHITE_PAWN);

        String url = "http://localhost:8080/";
        new PreLoginRepl(url).run();
    }
}
