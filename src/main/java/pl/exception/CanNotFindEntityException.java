package pl.exception;

import pl.user.friend_ship.FriendShip;

public class CanNotFindEntityException extends RuntimeException {
   public CanNotFindEntityException(Class<?> aClass) {
      super("Can not find entity" + aClass.getSimpleName());
   }
}
