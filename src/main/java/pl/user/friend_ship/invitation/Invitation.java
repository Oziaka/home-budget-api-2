package pl.user.friend_ship.invitation;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.user.User;

import javax.persistence.*;

@Data
@Entity(name = "invitation")
@NoArgsConstructor
public class Invitation {

  @Id
  @Column(name = "invitation_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private User inviter;

  @OneToOne
  private User invited;

  @Builder
  public Invitation(User inviter, User invited) {
    this.inviter = inviter;
    this.invited = invited;
  }
}