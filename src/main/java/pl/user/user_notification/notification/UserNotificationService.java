package pl.user.user_notification.notification;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.user.UserProvider;
import pl.user.user_notification.notification.notification.Notification;
import pl.user.user_notification.notification.notification.NotificationProvider;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserNotificationService {
  private UserNotificationRepository userNotificationRepository;
  private UserProvider userProvider;
  private NotificationProvider notificationProvider;

  List<UserNotificationDto> getNotifications(Principal principal, Pageable pageable, Specification<UserNotification> userNotificationSpecification) {
    User user = userProvider.get(principal);
    userNotificationSpecification.and(new IsUserNotification(user));
    return getAll(pageable, userNotificationSpecification).stream().map(UserNotificationMapper::toDto).collect(Collectors.toList());
  }

  UserNotificationDto updateStatus(Principal principal, Long userNotificationId, Status newStatus) {
    UserNotification userNotification = getOne(principal.getName(), userNotificationId);
    userNotification.setStatus(newStatus);
    UserNotification savedUserNotification = save(userNotification);
    return UserNotificationMapper.toDto(savedUserNotification);
  }

  private List<UserNotification> getAll(Pageable pageable, Specification<UserNotification> userNotificationSpecification) {
    return userNotificationRepository.findAll(userNotificationSpecification, pageable);
  }

  private UserNotification getOne(String email, Long userNotificationId) {
    return userNotificationRepository.getByUserEmailAndAndId(email, userNotificationId).orElseThrow(ThereIsNoYourPropertyException::new);
  }

  public UserNotification save(UserNotification userNotification) {
    return userNotificationRepository.save(userNotification);
  }

  public Notification sendNotificationToAllUser(Notification notification) {
    Notification savedNotification = notificationProvider.save(notification);
    for (User user : userProvider.getAll()) {
      userNotificationRepository.save(createUserNotification(notification, user));
    }
    return savedNotification;
  }

  private UserNotification createUserNotification(Notification notification, User user) {
    return UserNotification.builder()
      .notification(notification)
      .user(user)
      .build();
  }
}
