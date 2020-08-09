package pl.user.friend_ship.invitation;

public class InvitationMapper {
   public static InvitationDto toDto(Invitation invitation) {
      return InvitationDto.builder()
         .id(invitation.getId())
         .inviter(invitation.getInviter())
         .invited(invitation.getInvited())
         .build();
   }
}
