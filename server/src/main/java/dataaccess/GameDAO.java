package dataaccess;

import chess.ChessGame;
import responses.ListGameResponse;
import responses.ListGamesResponse;
import model.GameData;

import java.util.Collection;

public interface GameDAO extends DataDAO {
    public int createGame(String gameName) throws DataAccessException;

    public GameData getGame(int gameID) throws DataAccessException;

    public Collection<ListGameResponse> listGames() throws DataAccessException;

    public void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException;

    public void clear() throws DataAccessException;

    public boolean isEmpty() throws DataAccessException;
}