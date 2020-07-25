package pl.user.user_notification.notification;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import pl.user.User;
import pl.user.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class UserNotificationController {

    private UserNotificationService userNotificationService;
    private UserService userService;

    List<UserNotificationDto> getNotifications(Principal principal, Pageable pageable, Specification<UserNotification> userNotificationSpecification) {
        User user = userService.get(principal);
        userNotificationSpecification.and(new IsUserNotification(user));
        return userNotificationService.getAll(pageable, userNotificationSpecification).stream().map(UserNotificationMapper::toDto).collect(Collectors.toList());
    }

    UserNotificationDto updateStatus(Principal principal, Long userNotificationId, Status newStatus) {
        UserNotification userNotification = userNotificationService.getOne(principal.getName(), userNotificationId);
        userNotification.setStatus(newStatus);
        UserNotification savedUserNotification = userNotificationService.save(userNotification);
        return UserNotificationMapper.toDto(savedUserNotification);
    }
}
