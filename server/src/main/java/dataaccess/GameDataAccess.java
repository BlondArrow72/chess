package dataaccess;

import model.GameData;

import java.lang.Integer;
import java.util.HashMap;

public class GameDataAccess implements DataAccessObject{
    private final HashMap<Integer, GameData> gameDataDatabase = new HashMap<>();

    public void clear(){
        gameDataDatabase.clear();
    }
}
