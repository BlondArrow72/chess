package websocket.commands;

import chess.ChessMove;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class UserGameCommandTypeAdapter extends TypeAdapter<UserGameCommand> {
    @Override
    public void write(JsonWriter jsonWriter, UserGameCommand userGameCommand) throws IOException {
        Gson gson = new Gson();

        if (userGameCommand.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            gson.getAdapter(MakeMoveCommand.class).write(jsonWriter, (MakeMoveCommand) userGameCommand);
        } else {
            gson.toJson(userGameCommand);
        }
    }

    @Override
    public UserGameCommand read(JsonReader jsonReader) throws IOException {
        // setup variables
        UserGameCommand.CommandType commandType = null;
        String authToken = null;
        Integer gameID = null;
        ChessMove chessMove = null;
        Gson gson = new Gson();

        // begin reading
        jsonReader.beginObject();

        // read object
        while(jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "commandType" -> commandType = UserGameCommand.CommandType.valueOf(jsonReader.nextString());
                case "authToken"   -> authToken   = jsonReader.nextString();
                case "gameID"      -> gameID      = jsonReader.nextInt();
                case "chessMove"   -> chessMove = gson.fromJson(jsonReader, ChessMove.class);
            }
        }

        // end reading
        jsonReader.endObject();

        // create object
        if (commandType == UserGameCommand.CommandType.MAKE_MOVE) {
            return new MakeMoveCommand(commandType, authToken, gameID, chessMove);
        } else {
            return new UserGameCommand(commandType, authToken, gameID);
        }
    }
}
