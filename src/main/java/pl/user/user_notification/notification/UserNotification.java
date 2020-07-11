package pl.user.user_notification.notification;

import pl.user.Status;
import pl.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class UserNotification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_notification_id")
  private Long id;

  @ManyToOne
  private User user;

  @Enumerated
  private Status status;

  @OneToMany(mappedBy = "userNotification")
  private List<Notification> notifications;
}
