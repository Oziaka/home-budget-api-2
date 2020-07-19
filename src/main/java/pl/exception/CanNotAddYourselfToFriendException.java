package pl.exception;

public class CanNotAddYourselfToFriendException extends RuntimeException {
    public CanNotAddYourselfToFriendException() {
        super("Can not add yourself to friend");
    }
}
