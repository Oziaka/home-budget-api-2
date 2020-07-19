package pl.user.user_notification.notification;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.user.User;
import pl.user.user_notification.notification.UserNotificationRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserNotificationService {
    private UserNotificationRepository userNotificationRepository;

    List<UserNotification> getNotifications(Pageable pageable, Specification<UserNotification> userNotificationSpecification) {
        return userNotificationRepository.findAll(userNotificationSpecification, pageable);
    }

    Optional<UserNotification> getNotification(User user, Long userNotificationId) {
        return userNotificationRepository.getByUserAndId(user, userNotificationId);
    }

    public UserNotification save(UserNotification userNotification) {
        return userNotificationRepository.save(userNotification);
    }
}
