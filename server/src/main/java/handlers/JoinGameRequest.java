package handlers;

import chess.ChessGame.TeamColor;

public record JoinGameRequest(TeamColor playerColor, Integer gameID) {}
