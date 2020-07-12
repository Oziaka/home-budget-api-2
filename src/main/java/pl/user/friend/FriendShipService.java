package pl.user.friend;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class FriendShipService {

  private FriendShipRepository friendRepository;


  boolean areTheyAlreadyFriends (User user, User friend) {
    return friendRepository.findFirstByUserAndUser2(user, friend).isPresent();
  }

  public FriendShip save (FriendShip friendShip) {
    return friendRepository.save(friendShip);
  }

  public List<FriendShip> getFriendShipByUser (User user) {
    return friendRepository.findAllByUser(user);
  }
}
