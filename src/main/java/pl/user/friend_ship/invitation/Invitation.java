package pl.user.friend_ship.invitation;

import lombok.Builder;
import lombok.Data;
import pl.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity(name = "invitation")
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