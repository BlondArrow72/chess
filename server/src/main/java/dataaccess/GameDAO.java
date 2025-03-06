package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO extends DataDAO {
    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);

    public GameData getGame(int gameID);

    public Collection<GameData> listGames();

    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);

    public void clear();

    public boolean isEmpty();

    public int getGameID();
}