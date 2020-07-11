package pl.user.user_notification.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
}
