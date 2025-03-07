package handlers;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Error: bad request");
    }
}
