package pl.user.friend_ship.invitation;

import pl.user.User;
import pl.user.UserDto;

public class InvitationRandomTool {
   public static Invitation randomInvitation(User inviter, User invited) {
      return Invitation.builder().inviter(inviter).invited(invited).build();
   }

   public static Invitation randomInvitation(UserDto inviterDto, UserDto invitedDto) {
      return randomInvitation(User.builder().email(inviterDto.getEmail()).build(),User.builder().email(invitedDto.getEmail()).build());
   }
}
