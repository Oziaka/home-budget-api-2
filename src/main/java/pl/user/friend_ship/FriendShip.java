package pl.user.friend_ship;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "friend_ship")
@NoArgsConstructor
public class FriendShip {

   @Id
   @Column(name = "friend_ship_id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @OneToOne
   private User user;

   @OneToOne
   private User user2;

   private LocalDateTime dateOfAdding;

   @Builder
   public FriendShip(User user, User user2, LocalDateTime dateOfAdding) {
      this.user = user;
      this.user2 = user2;
      this.dateOfAdding = dateOfAdding;
   }
}
