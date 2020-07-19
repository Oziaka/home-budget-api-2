package pl.exception;

public class CanOnlySndOneInviteException extends RuntimeException {

    public CanOnlySndOneInviteException(){
        super("Can only send one invite to another user");
    }
}
