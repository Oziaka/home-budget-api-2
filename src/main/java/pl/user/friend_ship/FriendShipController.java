package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.exception.CanNotAddYourselfToFriendException;
import pl.exception.CanNotInviteAlreadyFriend;
import pl.exception.CanOnlySndOneInviteException;
import pl.exception.YourAreNotInvitedException;
import pl.user.User;
import pl.user.UserService;
import pl.user.friend_ship.friend.FriendDto;
import pl.user.friend_ship.friend.FriendMapper;
import pl.user.friend_ship.invitation.Invitation;
import pl.user.friend_ship.invitation.InvitationDto;
import pl.user.friend_ship.invitation.InvitationMapper;
import pl.user.friend_ship.invitation.InvitationService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class FriendShipController {

    private InvitationService invitationService;
    private UserService userService;
    private FriendShipService friendShipService;

    Object invite(Principal principal, String invitedUserEmail) {
        User inviter = userService.getUser(principal);
        User invited = userService.getUser(invitedUserEmail::toString);
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
        if (friendShipService.couldThisFriendshipExist(inviter, invited)) throw new CanNotInviteAlreadyFriend();
    }

    private void checkCouldUserDoNotAddYourself(User inviter, User invited) {
        if (invited.equals(inviter)) throw new CanNotAddYourselfToFriendException();
    }

    FriendShipDto add(Principal principal, Long invitationId) {
        User user = userService.getUser(principal);
        Invitation invitation = invitationService.getByInvited(user, invitationId);
        invitationService.remove(invitation);
        FriendShip friendShip = FriendShip.builder().user(invitation.getInvited()).user2(invitation.getInviter()).dateOfAdding(LocalDateTime.now()).build();
        FriendShip friendShip2 = FriendShip.builder().user(invitation.getInviter()).user2(invitation.getInvited()).dateOfAdding(LocalDateTime.now()).build();
        FriendShip savedFriendShip = friendShipService.save(friendShip);
        friendShipService.save(friendShip2);
        return FriendShipMapper.toDto(savedFriendShip);
    }

    List<FriendDto> getFriends(Principal principal) {
        User user = userService.getUser(principal);
        List<FriendShip> friendShips = friendShipService.getFriendShips(user);
        return friendShips.stream().map(FriendMapper::toDto).collect(Collectors.toList());
    }

    void remove(Principal principal, Long friendShipId) {
        User user = userService.getUser(principal);
        FriendShip friendShip = friendShipService.getFriendShip(user, friendShipId);
        FriendShip friendShip2 = friendShipService.getFriendShip(friendShip.getUser2(), user);
        friendShipService.remove(friendShip);
        friendShipService.remove(friendShip2);
//        TODO notification to second user
    }

    void removeInvitation(Principal principal, Long invitationId) {
        User inviter = userService.getUser(principal);
        Invitation invitation = invitationService.getByInviter(inviter, invitationId);
        invitationService.remove(invitation);
    }

    void cancelInvitation(Principal principal, Long invitationId) {
        User invited = userService.getUser(principal);
        Invitation invitation = invitationService.getByInvited(invited, invitationId);
        invitationService.remove(invitation);
//        TODO notification to inviter
    }

    public List<InvitationDto> getInvitationsFromUser(Principal principal) {
        User user = userService.getUser(principal);
        List<Invitation> allByInviter = invitationService.getAllByInviter(user);
        return allByInviter.stream().map(InvitationMapper::toDto).collect(Collectors.toList());
    }

    public List<InvitationDto> getInvitationsToUser(Principal principal) {
        User user = userService.getUser(principal);
        List<Invitation> allByInviter = invitationService.getAllByInviter(user);
        return allByInviter.stream().map(InvitationMapper::toDto).collect(Collectors.toList());
    }
}
