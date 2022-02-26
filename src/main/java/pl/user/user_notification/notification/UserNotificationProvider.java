package pl.user.user_notification.notification;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.user.User;
import pl.user.user_notification.notification.notification.Notification;
import pl.user.user_notification.notification.notification.NotificationProvider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserNotificationProvider {

    private UserNotificationRepository userNotificationRepository;
    private NotificationProvider notificationProvider;

    public void saveUserNotification(Notification notification, User... users) {
        saveUserNotification(notification, Arrays.stream(users).collect(Collectors.toList()));
    }

    public void saveUserNotification(Notification notification, List<User> users) {
        Notification savedNotification = notificationProvider.save(notification);
        for (User user : users) {
            userNotificationRepository.save(createUserNotification(notification, user));
        }
    }

    private UserNotification createUserNotification(Notification notification, User user) {
        return UserNotification.builder()
          .notification(notification)
          .user(user)
          .build();
    }
}
