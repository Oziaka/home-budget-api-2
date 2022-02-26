package pl.user.friend_ship;

import pl.exception.AppRuntimeException;

public class FriendShipException extends AppRuntimeException {
    public FriendShipException(FriendShipError friendShipError) {
        super(friendShipError);
    }
}
