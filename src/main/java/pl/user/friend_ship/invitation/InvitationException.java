package pl.user.friend_ship.invitation;

import pl.exception.AppRuntimeException;

public class InvitationException extends AppRuntimeException {
    public InvitationException(InvitationError invitationError) {
        super(invitationError);
    }
}
