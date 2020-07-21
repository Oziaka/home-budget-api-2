package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.CanNotFindEntityException;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendShipService {

    private FriendShipRepository friendShipRepository;

    public FriendShip save(FriendShip friendShip) {
        return friendShipRepository.save(friendShip);
    }

    public boolean couldThisFriendshipExist(User inviter, User invited) {
        return friendShipRepository.findAllByUserAndUser2(inviter, invited).isPresent();
    }

    List<FriendShip> getFriendShips(User user) {
        return friendShipRepository.findAllByUser(user);
    }

    FriendShip getFriendShip(User user, Long friendShipId) {
        return friendShipRepository.findByUserAndId(user, friendShipId).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    FriendShip getFriendShip(User user, User user2) {
        return friendShipRepository.findAllByUserAndUser2(user, user2).orElseThrow(() -> new CanNotFindEntityException(FriendShip.class));
    }

    void remove(FriendShip friendShip) {
        friendShipRepository.delete(friendShip);
    }
}
