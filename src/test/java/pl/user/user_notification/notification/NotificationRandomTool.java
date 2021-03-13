package pl.user.user_notification.notification;

import pl.user.user_notification.notification.notification.Notification;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static pl.tool.RandomUtils.randomString;

public class NotificationRandomTool {
   public static Notification randomNotification() {
      return Notification.builder()
         .tittle(randomString())
         .dateOfAdding(LocalDateTime.now())
         .items(Collections.singletonMap(randomString(), randomString()))
         .build();
   }

}
