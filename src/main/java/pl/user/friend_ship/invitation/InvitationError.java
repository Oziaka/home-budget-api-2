package pl.user.friend_ship.invitation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.exception.AppError;

@Getter
@AllArgsConstructor
public enum InvitationError implements AppError {
    NOT_FOUND("Invitation not found", HttpStatus.NOT_FOUND),
    NOT_YOUR_PROPERTY("Not you property", HttpStatus.BAD_REQUEST),
    CAN_NOT_ADD_YOURSELF_TO_FRIENDS("Can not add yourself to friend", HttpStatus.BAD_REQUEST),
    CAN_ONLY_SEND_ONE_INVITATION_TO_ONE_USER("Can only send one invite to another user", HttpStatus.BAD_REQUEST),
    CAN_NOT_SEND_INVITATION_TO_FRIEND("Can not invite already friend", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
