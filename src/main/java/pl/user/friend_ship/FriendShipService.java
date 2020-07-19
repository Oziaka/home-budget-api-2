package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.user.User;

@Service
@AllArgsConstructor
public class FriendShipService {

    private FriendShipRepository friendShipRepository;

    public FriendShip save(FriendShip friendShip) {
        return friendShipRepository.save(friendShip);
    }

    boolean couldThisFriendshipExist(User inviter, User invited) {
        return friendShipRepository.findAllByUserAndUser2(inviter, invited).isPresent();
    }
}
