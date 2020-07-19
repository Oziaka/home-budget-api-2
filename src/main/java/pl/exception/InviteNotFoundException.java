package pl.exception;

public class InviteNotFoundException extends RuntimeException {
    public InviteNotFoundException() {
        super("Can not find invite");
    }
}
