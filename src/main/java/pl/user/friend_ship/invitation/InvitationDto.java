package pl.user.friend_ship.invitation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.user.User;
import pl.user.UserDto;

@Getter
@EqualsAndHashCode
public class InvitationDto {

   private Long id;
   private UserDto inviter;
   private UserDto invited;

   @Builder
   public InvitationDto(Long id, UserDto inviter, UserDto invited) {
      this.id = id;
      this.inviter = inviter;
      this.invited = invited;
   }
}
