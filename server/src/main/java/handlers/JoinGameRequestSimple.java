package handlers;

import chess.ChessGame.TeamColor;

public record JoinGameRequestSimple(TeamColor playerColor, Integer gameID) {}
