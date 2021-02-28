package pl.user.friend_ship.invitation;

import pl.user.UserMapper;

public class InvitationMapper {
   public static InvitationDto toDto(Invitation invitation) {
      return InvitationDto.builder()
         .id(invitation.getId())
         .inviter(UserMapper.toDto(invitation.getInviter()))
         .invited(UserMapper.toDto(invitation.getInvited()))
         .build();
   }
}
