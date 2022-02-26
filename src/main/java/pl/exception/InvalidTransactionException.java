package pl.exception;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException() {
        super("Invalid transaction");
    }
}
