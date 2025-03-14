package handlers;

import chess.ChessGame.TeamColor;

public record JoinGameRequest(String authToken, TeamColor playerColor, Integer gameID) {}
