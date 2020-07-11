package pl.exception;

public class UserMustHaveUniqueEmailException extends RuntimeException {
  public UserMustHaveUniqueEmailException () {
    super("Your email is in use");
  }
}
