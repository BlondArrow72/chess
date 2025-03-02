package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.lang.Integer;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> memoryGameData = new HashMap<>();

    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        memoryGameData.put(gameID, newGame);
    }

    public GameData getGame(int gameID) {
        return memoryGameData.get(gameID);
    }

    public Collection<GameData> listGames() {
        return
    }

    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);

    public void clear() {
        memoryGameData.clear();
    }
}
