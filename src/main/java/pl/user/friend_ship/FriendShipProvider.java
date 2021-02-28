package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.user.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FriendShipProvider {

   private FriendShipRepository friendShipRepository;

   public FriendShip save(FriendShip friendShip) {
      return friendShipRepository.save(friendShip);
   }

   public boolean couldFriendshipExist(User user, User user2) {
      return friendShipRepository.findByUserAndUser2(user, user2).isPresent();
   }

   public boolean isFriends(User user, User user2) {
      return friendShipRepository.findByUserAndUser2(user, user2).isPresent();
   }

}
