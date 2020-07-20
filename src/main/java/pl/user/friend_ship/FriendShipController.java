package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
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
    private static int INVITATION_KEY_LENGTH = 30;


    Object invite(Principal principal, String invitedUserEmail) {
        User inviter = userService.getUser(principal);
        User invited = userService.getUser(invitedUserEmail::toString);
        checkCouldUserDoNotAddYourself(inviter, invited);
        Invitation invitation = Invitation.builder().inviter(inviter).invited(invited).key(generateKey()).build();
        checkCouldUserAlreadyInvite(inviter, invited);
        checkCouldUserDoNotInviteAlreadyFriend(inviter, invited);
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

    private String generateKey() {
        return RandomStringUtils.randomAlphabetic(INVITATION_KEY_LENGTH);
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

    FriendShipDto add(Principal principal, String key) {
        User user = userService.getUser(principal);
        Invitation invitation = invitationService.get(key);
        checkCouldUserIsInvited(user, invitation);
        invitationService.remove(invitation);
        FriendShip friendShip = FriendShip.builder().user(invitation.getInvited()).user2(invitation.getInviter()).dateOfAdding(LocalDateTime.now()).build();
        FriendShip friendShip2 = FriendShip.builder().user(invitation.getInviter()).user2(invitation.getInvited()).dateOfAdding(LocalDateTime.now()).build();
        FriendShip savedFriendShip = friendShipService.save(friendShip);
        friendShipService.save(friendShip2);
        return FriendShipMapper.toDto(savedFriendShip);
    }

    private void checkCouldUserIsInvited(User user, Invitation invitation) {
        if (!user.equals(invitation.getInvited())) throw new YourAreNotInvitedException();
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
    }
}
