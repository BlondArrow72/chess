package ui;

import chess.ChessGame.TeamColor;

public record GameplayTicket(String authToken, TeamColor playerColor, Integer gameID) {
}
