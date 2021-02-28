package pl.user.user_notification.notification;

public class UserNotificationMapper {

   public static UserNotificationDto toDto(UserNotification userNotification) {
      return UserNotificationDto.builder()
         .id(userNotification.getId())
         .description(userNotification.getNotification().getDescription())
         .dateOfAdding(userNotification.getNotification().getDateOfAdding())
         .status(userNotification.getStatus())
         .tittle(userNotification.getNotification().getTittle())
         .items(userNotification.getNotification().getItems())
         .build();
   }
}
