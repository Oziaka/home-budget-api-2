package pl.user.friend_ship.invitation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;

@AllArgsConstructor
@Service
public class InvitationProvider {
  private InvitationRepository invitationRepository;

  public Invitation getOneByInvited(User invited, Long invitationId) {
    return invitationRepository.findByInvitedAndId(invited, invitationId).orElseThrow(ThereIsNoYourPropertyException::new);
  }

  public void remove(Invitation invitation) {
    invitationRepository.delete(invitation);
  }

}
