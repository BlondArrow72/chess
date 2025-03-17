package ui;

import static ui.EscapeSequences.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class UiChessBoard {
    public static void main(String[] args) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        out.println("Chess Board" + WHITE_KING + "\n");
        drawBoard(out);
    }

    public static void drawBoard(PrintStream out) {
        // print header
        out.println("Print header");

        // print board
        out.println("Print boards");

        // print header
        out.println("Print header");
    }
}
