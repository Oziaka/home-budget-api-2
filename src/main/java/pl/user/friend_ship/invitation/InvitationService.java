package pl.user.friend_ship.invitation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.InviteNotFoundException;
import pl.user.User;

import javax.persistence.EntityExistsException;

@Service
@AllArgsConstructor
public class InvitationService {

    private InvitationRepository invitationRepository;


    public Invitation save(Invitation invitation) {
        return invitationRepository.save(invitation);
    }

    public boolean isExist(User inviter, User invited) {
        return invitationRepository.findAllByInviterAndInvited(inviter, invited).isPresent();
    }

    public Invitation get(User inviter, User invited) {
        return invitationRepository.findAllByInviterAndInvited(inviter, invited).orElseThrow(InviteNotFoundException::new);
    }

    public void remove(User inviter, User invited) {
        invitationRepository.removeByInviterAndInvited(inviter, invited);
    }
}
