package responses;

public record ListGamesResponse(int gameID, String whiteUsername, String blackUsername, String gameName) {}
