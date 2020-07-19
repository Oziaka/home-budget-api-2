package pl.user.user_notification.notification;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.user.UserService;
import pl.user.user_notification.notification.notification.NotificationService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class UserNotificationController {

    private UserNotificationService userNotificationService;
    private UserService userService;

    List<UserNotificationDto> getNotifications(Principal principal, Pageable pageable, Specification<UserNotification> userNotificationSpecification) {
        User user = userService.getUser(principal);
        userNotificationSpecification.and(new IsUserNotification());
        return userNotificationService.getNotifications(pageable, userNotificationSpecification).stream().map(UserNotificationMapper::toDto).collect(Collectors.toList());
    }


    UserNotificationDto updateStatus(Principal principal, Long userNotificationId, Status newStatus) {
        User user = userService.getUser(principal);
        UserNotification userNotification = userNotificationService.getNotification(user, userNotificationId).orElseThrow(ThereIsNoYourPropertyException::new);
        userNotification.setStatus(newStatus);
        UserNotification savedUserNotification = userNotificationService.save(userNotification);
        return UserNotificationMapper.toDto(savedUserNotification);
    }
}
