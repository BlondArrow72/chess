package service;

public class UnauthorizedUserError extends RuntimeException {
    public UnauthorizedUserError() {
        super("Error: unauthorized");
    }
}
