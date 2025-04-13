package websocket;

import chess.ChessPosition;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WebSocketHandlerTests {
    @Test
    public void chessPositionToStringTest() {
        AuthDAO testAuthDAO = new MemoryAuthDAO();
        GameDAO testGameDAO = new MemoryGameDAO();
        WebSocketHandler handler = new WebSocketHandler(testAuthDAO, testGameDAO);

        ChessPosition input = new ChessPosition(1, 1);
        String output = handler.chessPositionToString(input);

        Assertions.assertEquals("A1", output);
    }
}
