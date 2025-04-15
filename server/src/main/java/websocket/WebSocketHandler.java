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
            UserGameCommand userGameCommand = deserialize(command);

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

    private UserGameCommand deserialize(String command) {
        // register Gson type adapter
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserGameCommand.class, new UserGameCommandTypeAdapter());
        Gson gson = gsonBuilder.create();

        // do the deserialization
        return gson.fromJson(command, UserGameCommand.class);
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

            // if in checkmate or stalemate, don't allow a move
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)
                || game.isInCheckmate(ChessGame.TeamColor.BLACK)
                || game.isInStalemate(ChessGame.TeamColor.WHITE)
                || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                throw new InvalidMoveException("Game is over. Unable to make move.");
            }

            // check to make sure user making a move can make a move
            ChessGame.TeamColor teamTurn = game.getTeamTurn();
            if (teamTurn.equals(ChessGame.TeamColor.WHITE) && gameData.whiteUsername().equals(username)) {
                // make move in game
                game.makeMove(makeMoveCommand.getMove());
            } else if (teamTurn.equals(ChessGame.TeamColor.BLACK) && gameData.blackUsername().equals(username)) {
                // make move in game
                game.makeMove(makeMoveCommand.getMove());
            } else {
                // throw invalid move exception
                throw new InvalidMoveException("Not your turn or you're an observer.");
            }

            // load board for everyone
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            connectionManager.sendAll(loadGameMessage);

            // update game in database
            gameDAO.updateGame(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);

            // notify all players who didn't make the move
            String makeMoveMessage = username
                    + " moved from "
                    + chessPositionToString(makeMoveCommand.getMove().getStartPosition())
                    + " to "
                    + chessPositionToString(makeMoveCommand.getMove().getEndPosition());
            NotificationMessage notificationMessage = new NotificationMessage(makeMoveMessage);
            connectionManager.broadcast(username, notificationMessage);

            // end game if in checkmate
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                // broadcast message
                String checkmateBroadcast = username + " is in checkmate. Game over!";
                NotificationMessage checkmateNotification = new NotificationMessage(checkmateBroadcast);
                connectionManager.broadcast(username, checkmateNotification);

                // reply to user
                String checkmateReply = "You're in checkmate. Game over. Better luck next time!";
                NotificationMessage checkmateMessage = new NotificationMessage(checkmateReply);
                connectionManager.reply(username, checkmateMessage);
            }

            // end game if in stalemate
            if (game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                // broadcast message
                String stalemateBroadcast = username + " is in stalemate. Game over!";
                NotificationMessage stalemateNotification = new NotificationMessage(stalemateBroadcast);
                connectionManager.broadcast(username, stalemateNotification);

                // reply to user
                String stalemateReply = "You're in stalemate. Game over. Better luck next time!";
                NotificationMessage stalemateNotify = new NotificationMessage(stalemateReply);
                connectionManager.reply(username, stalemateNotify);
            }
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
