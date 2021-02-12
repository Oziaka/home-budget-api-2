package pl.user.user_notification;

import org.apache.commons.lang3.RandomUtils;
import pl.user.user_notification.notification.NotificationRandomTool;
import pl.user.user_notification.notification.Status;
import pl.user.user_notification.notification.UserNotification;

public class UserNotificationRandomTool {
   public static UserNotification randomUserNotification() {
      return UserNotification.builder()
         .notification(NotificationRandomTool.randomNotification())
         .status(randomUserNotificationStatus())
         .build();
   }

   public static UserNotification randomUserNotification(Long id) {
      UserNotification userNotification = randomUserNotification();
      userNotification.setId(id);
      return userNotification;
   }

   public static Status randomUserNotificationStatus() {
      return Status.values()[RandomUtils.nextInt(0, Status.values().length)];
   }
}
