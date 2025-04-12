package handlers;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.UserGameCommand;

public class WebSocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case CONNECT   -> connect( session, username, (ConnectCommand)  userGameCommand);
            case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) userGameCommand);
            case LEAVE     -> leave(   session, username, (LeaveCommand)    userGameCommand);
            case RESIGN    -> resign(  session, username, (ResignCommand)   userGameCommand);
        }
    }

    private void connect() {

    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }
}
