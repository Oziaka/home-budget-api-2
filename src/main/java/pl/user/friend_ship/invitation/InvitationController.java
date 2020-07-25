package pl.user.friend_ship.invitation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.exception.CanNotAddYourselfToFriendException;
import pl.exception.CanNotInviteAlreadyFriend;
import pl.exception.CanOnlySndOneInviteException;
import pl.user.User;
import pl.user.UserService;
import pl.user.friend_ship.FriendShip;
import pl.user.friend_ship.FriendShipMapper;
import pl.user.friend_ship.FriendShipService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class InvitationController {

    private InvitationService invitationService;
    private UserService userService;
    private FriendShipService friendShipService;

    Object invite(Principal principal, String invitedUserEmail) {
        User inviter = userService.get(principal);
        User invited = userService.get(invitedUserEmail::toString);
        checkCouldUserDoNotAddYourself(inviter, invited);
        Invitation invitation = Invitation.builder().inviter(inviter).invited(invited).build();
        checkCouldUserAlreadyInvite(inviter, invited);
        checkCouldUserDoNotInviteAlreadyFriend(inviter, invited);
//        TODO Notification to invited user
        if (checkCouldInvitedSendInviteEarly(invited, inviter)) {
            invitationService.remove(invited, inviter);
            FriendShip friendShip = friendShipService.save(FriendShip.builder().user(inviter).user2(invited).dateOfAdding(LocalDateTime.now()).build());
            friendShipService.save(FriendShip.builder().user(invited).user2(inviter).dateOfAdding(LocalDateTime.now()).build());
            return FriendShipMapper.toDto(friendShip);
        } else {
            Invitation savedInvitation = invitationService.save(invitation);
            return InvitationMapper.toDto(savedInvitation);
        }
    }

    private boolean checkCouldInvitedSendInviteEarly(User inviter, User invited) {
        return invitationService.isExist(invited, inviter);
    }


    private void checkCouldUserAlreadyInvite(User inviter, User invited) {
        if (invitationService.isExist(inviter, invited)) throw new CanOnlySndOneInviteException();
    }

    private void checkCouldUserDoNotInviteAlreadyFriend(User inviter, User invited) {
        if (friendShipService.couldFriendshipExist(inviter, invited)) throw new CanNotInviteAlreadyFriend();
    }

    private void checkCouldUserDoNotAddYourself(User inviter, User invited) {
        if (invited.equals(inviter)) throw new CanNotAddYourselfToFriendException();
    }

    void removeInvitation(Principal principal, Long invitationId) {
        User inviter = userService.get(principal);
        Invitation invitation = invitationService.getOneByInviter(inviter, invitationId);
        invitationService.remove(invitation);
    }

    void cancelInvitation(Principal principal, Long invitationId) {
        User invited = userService.get(principal);
        Invitation invitation = invitationService.getOneByInvited(invited, invitationId);
        invitationService.remove(invitation);
//        TODO notification to inviter
    }

    List<InvitationDto> getInvitationsFromUser(Principal principal) {
        User user = userService.get(principal);
        List<Invitation> allByInviter = invitationService.getAllByInviter(user);
        return allByInviter.stream().map(InvitationMapper::toDto).collect(Collectors.toList());
    }

    List<InvitationDto> getInvitationsToUser(Principal principal) {
        User user = userService.get(principal);
        List<Invitation> allByInviter = invitationService.getAllByInviter(user);
        return allByInviter.stream().map(InvitationMapper::toDto).collect(Collectors.toList());
    }
}
