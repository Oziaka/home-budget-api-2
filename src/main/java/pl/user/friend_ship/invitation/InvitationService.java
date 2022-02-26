package pl.user.friend_ship.invitation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.user.User;
import pl.user.UserProvider;
import pl.user.friend_ship.FriendShip;
import pl.user.friend_ship.FriendShipMapper;
import pl.user.friend_ship.FriendShipProvider;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvitationService {

    private InvitationRepository invitationRepository;
    private UserProvider userProvider;
    private FriendShipProvider friendShipProvider;

    Object invite(Principal principal, String invitedUserEmail) {
        User inviter = userProvider.get(principal);
        User invited = userProvider.get(invitedUserEmail::toString);
        if (checkCouldUserDoNotAddYourself(inviter, invited))
            throw new InvitationException(InvitationError.CAN_NOT_ADD_YOURSELF_TO_FRIENDS);
        if (checkCouldUserAlreadyInvite(inviter, invited))
            throw new InvitationException(InvitationError.CAN_ONLY_SEND_ONE_INVITATION_TO_ONE_USER);
        if (checkCouldUserDoNotInviteAlreadyFriend(inviter, invited))
            throw new InvitationException(InvitationError.CAN_NOT_SEND_INVITATION_TO_FRIEND);
//        TODO Notification to invited user
        if (checkCouldInvitedSendInviteEarly(inviter, invited)) {
            this.remove(invited, inviter);
            FriendShip friendShip = friendShipProvider.save(FriendShip.builder().user(inviter).user2(invited).dateOfAdding(LocalDateTime.now()).build());
            friendShipProvider.save(FriendShip.builder().user(invited).user2(inviter).dateOfAdding(LocalDateTime.now()).build());
            return FriendShipMapper.toDto(friendShip);
        } else {
            Invitation invitation = Invitation.builder().inviter(inviter).invited(invited).build();
            Invitation savedInvitation = this.save(invitation);
            return InvitationMapper.toDto(savedInvitation);
        }
    }

    private boolean checkCouldInvitedSendInviteEarly(User inviter, User invited) {
        return this.isExist(invited, inviter);
    }

    private boolean checkCouldUserAlreadyInvite(User inviter, User invited) {
        return this.isExist(inviter, invited);
    }

    private boolean checkCouldUserDoNotInviteAlreadyFriend(User inviter, User invited) {
        return friendShipProvider.couldFriendshipExist(inviter, invited);
    }

    private boolean checkCouldUserDoNotAddYourself(User inviter, User invited) {
        return invited.equals(inviter);
    }

    void cancelInvitation(Principal principal, Long invitationId) {
        User inviter = userProvider.get(principal);
        Invitation invitation = this.getOneByInviter(inviter, invitationId);
        this.remove(invitation);
    }

    void removeInvitation(Principal principal, Long invitationId) {
        User invited = userProvider.get(principal);
        Invitation invitation = this.getOneByInvited(invited, invitationId);
        this.remove(invitation);
//        TODO notification to inviter
    }

    List<InvitationDto> getInvitationsFromUser(Principal principal) {
        User user = userProvider.get(principal);
        List<Invitation> allByInviter = this.getAllByInviter(user);
        return allByInviter.stream().map(InvitationMapper::toDto).collect(Collectors.toList());
    }

    List<InvitationDto> getInvitationsToUser(Principal principal) {
        User user = userProvider.get(principal);
        List<Invitation> allByInviter = this.getAllByInvited(user);
        return allByInviter.stream().map(InvitationMapper::toDto).collect(Collectors.toList());
    }

    public Invitation save(Invitation invitation) {
        return invitationRepository.save(invitation);
    }

    public boolean isExist(User inviter, User invited) {
        return invitationRepository.findAllByInviterAndInvited(inviter, invited).isPresent();
    }

    public void remove(User inviter, User invited) {
        this.remove(invitationRepository.findAllByInviterAndInvited(inviter, invited).orElseThrow(() -> new InvitationException(InvitationError.NOT_FOUND)));
    }

    public void remove(Invitation invitation) {
        invitationRepository.delete(invitation);
    }

    public Invitation getOneByInviter(User inviter, Long invitationId) {
        return invitationRepository.findByInviterAndId(inviter, invitationId).orElseThrow(() -> new InvitationException(InvitationError.NOT_YOUR_PROPERTY));
    }

    public Invitation getOneByInvited(User invited, Long invitationId) {
        return invitationRepository.findByInvitedAndId(invited, invitationId).orElseThrow(() -> new InvitationException(InvitationError.NOT_YOUR_PROPERTY));
    }

    public List<Invitation> getAllByInviter(User inviter) {
        return invitationRepository.findAllByInviter(inviter);
    }

    public List<Invitation> getAllByInvited(User invited) {
        return invitationRepository.findAllByInvited(invited);
    }
}

