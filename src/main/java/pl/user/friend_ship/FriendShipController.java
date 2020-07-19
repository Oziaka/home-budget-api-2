package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import pl.exception.CanNotAddYourselfToFriendException;
import pl.exception.CanNotInviteAlreadyFriend;
import pl.exception.CanOnlySndOneInviteException;
import pl.user.User;
import pl.user.UserService;
import pl.user.friend_ship.invitation.Invitation;
import pl.user.friend_ship.invitation.InvitationMapper;
import pl.user.friend_ship.invitation.InvitationService;

import java.security.Principal;

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
        Invitation invitation = Invitation.builder().inviter(inviter).invited(invited).key(generateKey()).build();
        checkCouldUserAlreadyInvite(inviter, invited);
        checkCouldUserDoNotInviteAlreadyFriend(inviter, invited);
        if (checkCouldInvitedSendInviteEarly(invited, inviter)) {
            invitationService.remove(invited, inviter);
            FriendShip friendShip = friendShipService.save(FriendShip.builder().user(inviter).user2(invited).build());
            friendShipService.save(FriendShip.builder().user(invited).user2(inviter).build());
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
        return RandomStringUtils.randomAlphabetic(30);
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

    public FriendShipDto add(Principal principal, String key) {

        return null;
    }
}
