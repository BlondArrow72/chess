package handlers;

public record ListGamesResponse(int gameID, String whiteUsername, String blackUsername, String gameName) {}
