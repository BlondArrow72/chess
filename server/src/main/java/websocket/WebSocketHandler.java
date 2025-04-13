package websocket;

import chess.ChessGame;

import dataaccess.DataAccessException;

import model.AuthData;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.UnauthorizedUserError;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;


    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            // deserialize object
            UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);

            // verify authToken
            String authToken = userGameCommand.getAuthToken();
            AuthData authData = authDAO.getAuth(authToken);
            if (!authToken.equals(authData.authToken())) {
                throw new UnauthorizedUserError();
            }

            // send to sub methods
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect(session, authData.username(), userGameCommand);
                case MAKE_MOVE -> makeMove(session, (MakeMoveCommand) userGameCommand);
                case LEAVE -> leave(session, userGameCommand);
                case RESIGN -> resign(session, userGameCommand);
            }
        } catch (UnauthorizedUserError | DataAccessException | IOException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            String errorMessageJson = new Gson().toJson(errorMessage);
            session.getRemote().sendString(errorMessageJson);
        }
    }

    private void connect(Session session, String username, UserGameCommand userGameCommand) throws IOException {
        // add connection to websocket
        connectionManager.add(username, session);

        // send game to client
        GameData gameData;
        try {
            gameData = gameDAO.getGame(userGameCommand.getGameID());
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            connectionManager.reply(username, errorMessage);
            return;
        }

        ChessGame game = gameData.game();
        LoadGameMessage loadGameMessage = new LoadGameMessage(game);
        String loadGameJson = new Gson().toJson(loadGameMessage);
        session.getRemote().sendString(loadGameJson);

        // broadcast all other clients that user joined
        String notificationString = username + " has joined the game";
        NotificationMessage notificationMessage = new NotificationMessage(notificationString);
        connectionManager.broadcast(username, notificationMessage);
    }

    private void makeMove(Session session, MakeMoveCommand makeMoveCommand) {

    }

    private void leave(Session session, UserGameCommand userGameCommand) {

    }

    private void resign(Session session, UserGameCommand userGameCommand) {

    }
}
