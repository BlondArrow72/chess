package serverFacade;

public class ResponseException extends RuntimeException {
    public ResponseException(int ErrorCode, String message) {
        super(message);
    }
}
