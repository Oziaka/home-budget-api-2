package pl.exception;

public class YouAreNotFriendsException extends RuntimeException {
   public YouAreNotFriendsException() {
      super("You are not friends");
   }

}
