package websocket;

import chess.ChessGame;
import chess.ChessPosition;
import chess.InvalidMoveException;

import com.google.gson.*;

import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;

import model.AuthData;
import model.GameData;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import service.UnauthorizedUserError;

import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.commands.UserGameCommand.CommandType;
import websocket.commands.UserGameCommandTypeAdapter;
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
    public void onMessage(Session session, String command) throws IOException {
        try {
            // deserialize object
            deserialize(command);
            String type = command.get("commandType").getAsString();

            UserGameCommand userGameCommand = new Gson().fromJson(command, UserGameCommand.class);

            // verify authToken
            String authToken = userGameCommand.getAuthToken();
            AuthData authData = authDAO.getAuth(authToken);
            if (!authToken.equals(authData.authToken())) {
                throw new UnauthorizedUserError();
            }

            // send to sub methods
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect(session, authData.username(), userGameCommand);
                case MAKE_MOVE -> makeMove(session, authData.username(), (MakeMoveCommand) userGameCommand);
                case LEAVE -> leave(session, userGameCommand);
                case RESIGN -> resign(session, userGameCommand);
            }
        } catch (NullPointerException | UnauthorizedUserError | DataAccessException | IOException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            String errorMessageJson = new Gson().toJson(errorMessage);
            session.getRemote().sendString(errorMessageJson);
        } catch (Exception e) {
            System.err.println("Error in WebSocketHandler: " + e.getMessage());
            e.printStackTrace();

            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            String errorMessageJson = new Gson().toJson(errorMessage);
            session.getRemote().sendString(errorMessageJson);
        }
    }

    private deserialize(String command) {
        // register Gson type adapter
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserGameCommand.class, UserGameCommandTypeAdapter);
        Gson gson = gsonBuilder.create();

        // TODO: finish implementation
    }

    private void connect(Session session, String username, UserGameCommand userGameCommand) throws IOException {
        try {
            // add connection to websocket
            connectionManager.add(username, session);

            // get game
            GameData gameData = gameDAO.getGame(userGameCommand.getGameID());
            ChessGame game = gameData.game();

            // make loadGameMessage
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            String loadGameJson = new Gson().toJson(loadGameMessage);

            // send loadGameMessage
            session.getRemote().sendString(loadGameJson);

            // broadcast all other clients that user joined
            String notificationString = username + " has joined the game.";
            NotificationMessage notificationMessage = new NotificationMessage(notificationString);
            connectionManager.broadcast(username, notificationMessage);
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            connectionManager.reply(username, errorMessage);
        }
    }

    private void makeMove(Session session, String username, MakeMoveCommand makeMoveCommand) throws IOException {
        try {
            // get game
            GameData gameData = gameDAO.getGame(makeMoveCommand.getGameID());
            ChessGame game = gameData.game();

            // make move in game
            game.makeMove(makeMoveCommand.getChessMove());

            // load board for everyone
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            connectionManager.sendAll(loadGameMessage);

            // notify all players who didn't make the move
            String makeMoveMessage = username
                    + " moved from "
                    + chessPositionToString(makeMoveCommand.getChessMove().getStartPosition())
                    + " to "
                    + chessPositionToString(makeMoveCommand.getChessMove().getEndPosition());
            NotificationMessage notificationMessage = new NotificationMessage(makeMoveMessage);
            connectionManager.broadcast(username, notificationMessage);

        } catch (DataAccessException | InvalidMoveException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            String errorMessageJson = new Gson().toJson(errorMessage);
            session.getRemote().sendString(errorMessageJson);
        }
    }

    public String chessPositionToString(ChessPosition chessPosition) {
        // convert col
        int colInt = chessPosition.getColumn();
        char colChar = (char)('A' + colInt - 1);

        // convert row
        int rowInt = chessPosition.getRow();

        return "" + colChar + rowInt;
    }

    private void leave(Session session, UserGameCommand userGameCommand) {

    }

    private void resign(Session session, UserGameCommand userGameCommand) {

    }
}
