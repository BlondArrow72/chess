package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.lang.Integer;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> memoryGameData;
    private int gameID;

    public MemoryGameDAO() {
        memoryGameData = new HashMap<>();
        gameID = 1;
    }

    public void createGame(GameData newGame) {
        memoryGameData.put(newGame.gameID(), newGame);
    }

    public GameData getGame(int gameID) {
        return memoryGameData.get(gameID);
    }

    public Collection<GameData> listGames() {
        return memoryGameData.values();
    }

    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        GameData updatedGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        memoryGameData.put(gameID, updatedGame);
    }

    public void clear() {
        memoryGameData.clear();
    }

    public boolean isEmpty() {
        return memoryGameData.isEmpty();
    }

    public int getGameID() {
        return gameID++;
    }
}
