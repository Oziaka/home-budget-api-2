package pl.exception;

public class UserRoleOfSuchIdDoNotExistException extends RuntimeException {
   public UserRoleOfSuchIdDoNotExistException() {
      super("User role of such id do not exist");
   }
}
