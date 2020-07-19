package pl.user.friend_ship.invitation;

import lombok.Builder;
import lombok.Getter;
import pl.user.User;

@Getter
public class InvitationDto {

    @Builder
    public InvitationDto(Long id, User inviter, User invited, String key) {
        this.id = id;
        this.inviter = inviter;
        this.invited = invited;
        this.key = key;
    }

    private Long id;

    private User inviter;

    private User invited;

    private String key;
}
