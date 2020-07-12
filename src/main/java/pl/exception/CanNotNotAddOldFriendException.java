package pl.exception;

public class CanNotNotAddOldFriendException extends RuntimeException {
  public CanNotNotAddOldFriendException () {
    super("User already have this user as friend");
  }
}
