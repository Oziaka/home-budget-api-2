package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.CanNotFindEntityException;
import pl.exception.ThereIsNoYourPropertyException;
import pl.exception.YouAreNotFriendsException;
import pl.user.User;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendShipService {

   private FriendShipRepository friendShipRepository;

   public FriendShip save(FriendShip friendShip) {
      return friendShipRepository.save(friendShip);
   }

   public boolean couldFriendshipExist(User user, User user2) {
      return friendShipRepository.findAllByUserAndUser2(user, user2).isPresent();
   }

   List<FriendShip> getAll(String email) {
      return friendShipRepository.findALlByUserEmail(email);
   }

   FriendShip getOne(User user, Long friendShipId) {
      return friendShipRepository.findByUserAndId(user, friendShipId).orElseThrow(ThereIsNoYourPropertyException::new);
   }

   FriendShip getOne(User user, User user2) {
      return friendShipRepository.findAllByUserAndUser2(user, user2).orElseThrow(() -> new CanNotFindEntityException(FriendShip.class));
   }

   void remove(FriendShip friendShip) {
      friendShipRepository.delete(friendShip);
   }

   public boolean isFriends(User user, User user2) {
      friendShipRepository.findAllByUserAndUser2(user, user2).orElseThrow(YouAreNotFriendsException::new);
      return true;
   }
}
