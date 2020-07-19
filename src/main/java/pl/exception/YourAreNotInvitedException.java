package pl.exception;

public class YourAreNotInvitedException extends RuntimeException {
    public YourAreNotInvitedException() {
        super("Your are not invited");
    }
}

