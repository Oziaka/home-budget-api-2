package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.user.User;

import java.util.List;

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

    public List<FriendShip> getFriendShips(User user) {
        return friendShipRepository.findAllByUser(user);
    }
}
