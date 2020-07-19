package pl.exception;

public class ThereIsNoWalletsPropertyException extends RuntimeException {
    public ThereIsNoWalletsPropertyException(String message) {
        super(message);
    }
}
