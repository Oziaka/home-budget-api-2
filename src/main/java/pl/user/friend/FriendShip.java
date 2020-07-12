package pl.user.friend;

import lombok.*;
import pl.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "friend")
public class FriendShip {

  @OneToOne
  public User user;

  @OneToOne
  public User user2;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "friend_id")
  private Long id;
  private LocalDateTime dateOfMakingFiends;

  @Builder
  public FriendShip (User user, User user2, LocalDateTime dateOfMakingFiends) {
    this.user = user;
    this.user2 = user2;
    this.dateOfMakingFiends = dateOfMakingFiends;
  }
}
