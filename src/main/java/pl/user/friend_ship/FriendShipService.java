package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.user.User;
import pl.user.UserProvider;
import pl.user.friend_ship.friend.FriendDto;
import pl.user.friend_ship.friend.FriendMapper;
import pl.user.friend_ship.invitation.Invitation;
import pl.user.friend_ship.invitation.InvitationProvider;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FriendShipService {

    private InvitationProvider invitationProvider;
    private UserProvider userProvider;
    private FriendShipRepository friendShipRepository;

    FriendShipDto add(Principal principal, Long invitationId) {
        User user = userProvider.get(principal);
        Invitation invitation = invitationProvider.getOneByInvited(user, invitationId);
        invitationProvider.remove(invitation);
        FriendShip friendShip = FriendShip.builder().user(invitation.getInvited()).user2(invitation.getInviter()).dateOfAdding(LocalDateTime.now()).build();
        FriendShip friendShip2 = FriendShip.builder().user(invitation.getInviter()).user2(invitation.getInvited()).dateOfAdding(friendShip.getDateOfAdding()).build();
        FriendShip savedFriendShip = this.save(friendShip);
        this.save(friendShip2);
//      TODO tell second user by notification
        return FriendShipMapper.toDto(savedFriendShip);
    }

    List<FriendDto> getFriends(Principal principal) {
        User user = userProvider.get(principal);
        return this.getAll(user).stream().map(FriendMapper::toDto).collect(Collectors.toList());
    }

    void remove(Principal principal, Long friendShipId) {
        User user = userProvider.get(principal);
        FriendShip friendShip = this.getOne(user, friendShipId);
        FriendShip friendShip2 = this.getOne(friendShip.getUser2(), user);
        this.remove(friendShip);
        this.remove(friendShip2);
//        TODO notification to second user
    }

    public FriendShip save(FriendShip friendShip) {
        return friendShipRepository.save(friendShip);
    }

    private List<FriendShip> getAll(User user) {
        return friendShipRepository.findALlByUser(user);
    }

    private FriendShip getOne(User user, Long friendShipId) {
        return friendShipRepository.findByUserAndId(user, friendShipId).orElseThrow(() -> new FriendShipException(FriendShipError.NOT_FOUND));
    }

    private FriendShip getOne(User user, User user2) {
        return friendShipRepository.findByUserAndUser2(user, user2).orElseThrow(() -> new FriendShipException(FriendShipError.NOT_FOUND));
    }

    private void remove(FriendShip friendShip) {
        friendShipRepository.delete(friendShip);
    }

}
