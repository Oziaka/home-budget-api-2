package pl.user.user_notification.notification;

import pl.exception.AppError;
import pl.exception.AppRuntimeException;

public class UserNotificationException extends AppRuntimeException {
    public UserNotificationException(UserNotificationError userNotificationError) {
        super(userNotificationError);
    }
}
