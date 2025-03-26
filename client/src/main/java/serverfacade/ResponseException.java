package serverfacade;

public class ResponseException extends RuntimeException {
    public ResponseException(int errorCode, String message) {
        super(message);
    }
}
