package pl.exception;

public class InvalidInvitationKeyException extends RuntimeException {

    public InvalidInvitationKeyException() {
        super("Invalid invitation key");
    }

}
