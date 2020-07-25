package pl.user.friend_ship.invitation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.InvalidInvitationKeyException;
import pl.exception.InviteNotFoundException;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;

import java.util.List;

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

    public void remove(User inviter, User invited) {
        invitationRepository.removeByInviterAndInvited(inviter, invited);
    }

    public void remove(Invitation invitation) {
        invitationRepository.delete(invitation);
    }

    public Invitation getOneByInviter(User inviter, Long invitationId) {
        return invitationRepository.findByInviterAndId(inviter, invitationId).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    public Invitation getOneByInvited(User invited, Long invitationId) {
        return invitationRepository.findByInviterAndId(invited, invitationId).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    public List<Invitation> getAllByInviter(User inviter) {
        return invitationRepository.findAllByInviter(inviter);
    }

    public List<Invitation> getAllByInvited(User invited) {
        return invitationRepository.findAllByInvited(invited);
    }
}

