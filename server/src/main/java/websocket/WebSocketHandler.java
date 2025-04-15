package websocket;

import chess.ChessGame;
import chess.ChessPosition;
import chess.InvalidMoveException;

import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;

import service.UnauthorizedUserError;

import model.AuthData;
import model.GameData;

import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.commands.UserGameCommandTypeAdapter;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;

@WebSocket
public class WebSocketHandler {

    private final Map<Integer, ConnectionManager> connectionManagerMap = new HashMap<>();
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
                case CONNECT   -> connect(session, authData.username(), userGameCommand);
                case MAKE_MOVE -> makeMove(session, authData.username(), (MakeMoveCommand) userGameCommand);
                case LEAVE     -> leave(session, authData.username(), userGameCommand);
                case RESIGN    -> resign(session, authData.username(), userGameCommand);
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
            // check if connectionManager already exists for game
            ConnectionManager connectionManager = connectionManagerMap.get(userGameCommand.getGameID());

            if (connectionManager == null) {
                // create new connection manager
                connectionManager = new ConnectionManager();
            }

            // add connection to game map
            connectionManager.add(username, session);

            // update connectionManagerMap
            connectionManagerMap.put(userGameCommand.getGameID(), connectionManager);

            // get game
            GameData gameData = gameDAO.getGame(userGameCommand.getGameID());
            ChessGame game = gameData.game();

            // make loadGameMessage
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            String loadGameJson = new Gson().toJson(loadGameMessage);

            // send loadGameMessage
            session.getRemote().sendString(loadGameJson);

            // broadcast all other clients that user joined
            String notificationString;
            if (gameData.whiteUsername().equals(username)) {
                notificationString = username + " has joined the game as white.";
            } else if (gameData.blackUsername().equals(username)) {
                notificationString = username + " has joined the game as black.";
            } else {
                notificationString = username + " has joined the game as an observer.";
            }
            NotificationMessage notificationMessage = new NotificationMessage(notificationString);
            connectionManager.broadcast(username, notificationMessage);
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            ConnectionManager connectionManager = connectionManagerMap.get(userGameCommand.getGameID());
            connectionManager.reply(username, errorMessage);
        }
    }

    private void makeMove(Session session, String username, MakeMoveCommand makeMoveCommand) throws IOException {
        try {
            // get connection manager
            ConnectionManager connectionManager = connectionManagerMap.get(makeMoveCommand.getGameID());

            // get game
            GameData gameData = gameDAO.getGame(makeMoveCommand.getGameID());
            ChessGame game = gameData.game();

            // if player has resigned, don't allow move
            if (gameData.whiteUsername() == null && gameData.blackUsername() == null) {
                throw new InvalidMoveException("Cannot make move after resigning.");
            }

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
                return;
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
                return;
            }

            // notify if in check
            if (game.isInCheck(ChessGame.TeamColor.WHITE) || game.isInCheck(ChessGame.TeamColor.BLACK)) {
                // broadcast message
                String checkBroadcast = username + " is in check. Oh no!";
                NotificationMessage checkNotification = new NotificationMessage(checkBroadcast);
                connectionManager.broadcast(username, checkNotification);

                // reply to user
                String checkReply = "You're in check. Get out!";
                NotificationMessage checkNotify = new NotificationMessage(checkReply);
                connectionManager.reply(username, checkNotify);
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

    private void leave(Session session, String username, UserGameCommand userGameCommand) throws IOException {
        try {
            // get connectionManager
            ConnectionManager connectionManager = connectionManagerMap.get(userGameCommand.getGameID());

            // update game to make sure username is null
            GameData gameData = gameDAO.getGame(userGameCommand.getGameID());

            if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
                gameDAO.updateGame(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)) {
                gameDAO.updateGame(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            }

            // broadcast that user is leaving the game
            String leaveString = username + " is leaving the game.";
            NotificationMessage leaveMessage = new NotificationMessage(leaveString);
            connectionManager.broadcast(username, leaveMessage);

            // disconnect the user from the game
            connectionManager.remove(username);
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            String errorMessageJson = new Gson().toJson(errorMessage);
            session.getRemote().sendString(errorMessageJson);
        }
    }

    private void resign(Session session, String username, UserGameCommand userGameCommand) throws IOException {
        try {
            // get connectionManager
            ConnectionManager connectionManager = connectionManagerMap.get(userGameCommand.getGameID());

            // update the game
            GameData gameData = gameDAO.getGame(userGameCommand.getGameID());
            if ((gameData.whiteUsername() != null && gameData.whiteUsername().equals(username))
                    || (gameData.whiteUsername() != null && gameData.blackUsername().equals(username))) {
                gameDAO.updateGame(gameData.gameID(), null, null, gameData.gameName(), gameData.game());
            } else {
                String errorString = "Observers cannot resign from game.";
                ErrorMessage errorMessage = new ErrorMessage(errorString);
                String errorMessageJson = new Gson().toJson(errorMessage);
                session.getRemote().sendString(errorMessageJson);
                return;
            }

            // broadcast resignation
            String resignString = username + " is resigning from the game.";
            NotificationMessage resignNotification = new NotificationMessage(resignString);
            connectionManager.notifyAll(resignNotification);

            // remove user from connectionManager
            connectionManager.remove(username);
        } catch (DataAccessException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            String errorMessageJson = new Gson().toJson(errorMessage);
            session.getRemote().sendString(errorMessageJson);
        }
    }
}
