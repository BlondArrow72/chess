package service;

public class UnauthorizedUserError extends RuntimeException {
    public UnauthorizedUserError(String message) {
        super(message);
    }
}
