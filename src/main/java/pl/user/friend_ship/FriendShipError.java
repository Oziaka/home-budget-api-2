package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.exception.AppError;

@Getter
@AllArgsConstructor
public enum FriendShipError implements AppError {
    NOT_FOUND("Friend ship role not found", HttpStatus.NOT_FOUND),
    NOT_YOUR_PROPERTY("Not you property", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
