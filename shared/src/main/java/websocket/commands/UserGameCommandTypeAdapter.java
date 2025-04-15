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

        jsonWriter.beginObject();

        jsonWriter.name("commandType").value(userGameCommand.getCommandType().toString());
        jsonWriter.name("authToken").value(userGameCommand.getAuthToken());
        jsonWriter.name("gameID").value(userGameCommand.getGameID());

        if (userGameCommand.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            MakeMoveCommand moveCommand = (MakeMoveCommand) userGameCommand;
            jsonWriter.name("move");
            gson.toJson(moveCommand.getMove(), ChessMove.class, jsonWriter);
        }

        jsonWriter.endObject();
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
                case "move"        -> chessMove = gson.fromJson(jsonReader, ChessMove.class);
            }
        }

        // end reading
        jsonReader.endObject();

        // create object
        if (commandType == UserGameCommand.CommandType.MAKE_MOVE && chessMove != null) {
            return new MakeMoveCommand(commandType, authToken, gameID, chessMove);
        } else {
            return new UserGameCommand(commandType, authToken, gameID);
        }
    }
}
