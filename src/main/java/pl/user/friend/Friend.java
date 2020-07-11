package pl.user.friend;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.user.User;

import javax.persistence.*;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "friend")
public class Friend {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "friend_id")
  private Long id;

  @Column(nullable = false)
  public User user;

  @Column(nullable = false)
  public User friend;
}
