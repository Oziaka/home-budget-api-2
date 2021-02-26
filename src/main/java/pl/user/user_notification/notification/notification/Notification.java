package pl.user.user_notification.notification.notification;

import lombok.*;
import pl.user.user_notification.notification.UserNotification;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification")
public class Notification {

   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "notification_id")
   @Id
   private Long id;

   private String tittle;

   private String description;

   private LocalDateTime dateOfAdding;

   @OneToMany(mappedBy = "notification")
   private List<UserNotification> userNotification;

   @ElementCollection
   @CollectionTable(name = "notification_item",
      joinColumns = {@JoinColumn(name = "id_notification", referencedColumnName = "notification_id")})
   @MapKeyColumn(name = "\"key\"")
   @Column(name = "value")
   private Map<String, String> items;

   @Builder
   public Notification(String tittle, String description, LocalDateTime dateOfAdding, Map<String, String> items) {
      this.tittle = tittle;
      this.description = description;
      this.dateOfAdding = dateOfAdding;
      this.items = items;
   }

   public void addItem(String key, String value) {
      if (items == null)
         items = new HashMap<>();
      items.compute(key, (k, l) -> value);
   }
}
