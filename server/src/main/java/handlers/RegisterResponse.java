package handlers;

import java.util.List;

public record RegisterResponse (int statusCode, List<String> message) {}
