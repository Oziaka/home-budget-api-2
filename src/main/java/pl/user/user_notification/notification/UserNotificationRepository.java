package pl.user.user_notification.notification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {


    List<UserNotification> findAll(Specification<UserNotification> userNotificationSpecification, Pageable pageable);

    Optional<UserNotification> getByUserAndId(User user, Long userNotificationId);
}
