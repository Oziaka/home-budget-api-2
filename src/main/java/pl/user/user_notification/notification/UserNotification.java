package pl.user.user_notification.notification;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.user.User;
import pl.user.user_notification.notification.notification.Notification;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
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

    @Builder
    public UserNotification(User user, Status status, Notification notification) {
        this.user = user;
        this.status = status;
        this.notification = notification;
    }
}
