package pl.user.user_notification.notification;

import lombok.Data;
import pl.user.User;
import pl.user.user_notification.notification.notification.Notification;

import javax.persistence.*;

@Entity
@Data
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_notification_id")
    private Long id;

    @ManyToOne
    private User user;

    @Enumerated
    private Status status;

    @ManyToOne
    private Notification notification;
}
