package pl.user.user_notification.notification;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserNotificationService {
    private UserNotificationRepository userNotificationRepository;

    List<UserNotification> getAll(Pageable pageable, Specification<UserNotification> userNotificationSpecification) {
        return userNotificationRepository.findAll(userNotificationSpecification, pageable);
    }

    UserNotification getOne(String email, Long userNotificationId) {
        return userNotificationRepository.getByUserEmailAndAndId(email, userNotificationId).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    public UserNotification save(UserNotification userNotification) {
        return userNotificationRepository.save(userNotification);
    }
}
