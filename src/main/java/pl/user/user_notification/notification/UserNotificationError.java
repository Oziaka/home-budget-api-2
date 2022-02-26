package pl.user.user_notification.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.exception.AppError;

@Getter
@AllArgsConstructor
public enum UserNotificationError implements AppError {
    NO_YOUR_PROPERTY("There is no your property", HttpStatus.BAD_REQUEST);
    private String message;
    private HttpStatus status;
}
