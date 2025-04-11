package dataaccess;

import chess.ChessGame;
import responses.ListGamesResponse;
import model.GameData;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> memoryGameData;
    private int gameID;

    public MemoryGameDAO() {
        memoryGameData = new HashMap<>();
        gameID = 1;
    }

    public int createGame(String gameName) throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData newGame = new GameData(gameID, null, null, gameName, chessGame);
        memoryGameData.put(newGame.gameID(), newGame);
        return gameID++;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return memoryGameData.get(gameID);
    }

    public Collection<ListGamesResponse> listGames() throws DataAccessException {
        Collection<GameData> gameDataCollection = memoryGameData.values();

        Collection<ListGamesResponse> listGamesResponseCollection = new ArrayList<>();
        for (GameData game : gameDataCollection) {
            int gameID = game.gameID();
            String whiteUsername = game.whiteUsername();
            String blackUsername = game.blackUsername();
            String gameName = game.gameName();
            ListGamesResponse listGamesResponse = new ListGamesResponse(gameID, whiteUsername, blackUsername, gameName);
            listGamesResponseCollection.add(listGamesResponse);
        }

        return listGamesResponseCollection;
    }

    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        GameData updatedGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        memoryGameData.put(gameID, updatedGame);
    }

    public void clear() throws DataAccessException {
        memoryGameData.clear();
    }

    public boolean isEmpty() throws DataAccessException {
        return memoryGameData.isEmpty();
    }
}
