package pl.exception;

public class CanNotInviteAlreadyFriend extends RuntimeException {
   public CanNotInviteAlreadyFriend() {
      super("Can not invite already friend");
   }
}
