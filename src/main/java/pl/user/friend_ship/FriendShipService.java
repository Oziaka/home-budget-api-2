package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.user.UserService;
import pl.user.friend_ship.friend.FriendDto;
import pl.user.friend_ship.friend.FriendMapper;
import pl.user.friend_ship.invitation.Invitation;
import pl.user.friend_ship.invitation.InvitationService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FriendShipService {

   private InvitationService invitationService;
   private UserService userService;
   private FriendShipRepository friendShipRepository;

   FriendShipDto add(Principal principal, Long invitationId) {
      User user = userService.getUser(principal);
      Invitation invitation = invitationService.getOneByInvited(user, invitationId);
      invitationService.remove(invitation);
      FriendShip friendShip = FriendShip.builder().user(invitation.getInvited()).user2(invitation.getInviter()).dateOfAdding(LocalDateTime.now()).build();
      FriendShip friendShip2 = FriendShip.builder().user(invitation.getInviter()).user2(invitation.getInvited()).dateOfAdding(LocalDateTime.now()).build();
      FriendShip savedFriendShip = this.save(friendShip);
      this.save(friendShip2);
      return FriendShipMapper.toDto(savedFriendShip);
   }

   List<FriendDto> getFriends(Principal principal) {
      return this.getAll(principal.getName()).stream().map(FriendMapper::toDto).collect(Collectors.toList());
   }

   void remove(Principal principal, Long friendShipId) {
      User user = userService.getUser(principal);
      FriendShip friendShip = this.getOne(user, friendShipId);
      FriendShip friendShip2 = this.getOne(friendShip.getUser2(), user);
      this.remove(friendShip);
      this.remove(friendShip2);
//        TODO notification to second user
   }

   public FriendShip save(FriendShip friendShip) {
      return friendShipRepository.save(friendShip);
   }

   private List<FriendShip> getAll(String email) {
      return friendShipRepository.findALlByUserEmail(email);
   }

   private FriendShip getOne(User user, Long friendShipId) {
      return friendShipRepository.findByUserAndId(user, friendShipId).orElseThrow(ThereIsNoYourPropertyException::new);
   }

   private FriendShip getOne(User user, User user2) {
      return friendShipRepository.findAllByUserAndUser2(user, user2).orElseThrow(() -> new RuntimeException("Friend ship not found"));
   }

   private void remove(FriendShip friendShip) {
      friendShipRepository.delete(friendShip);
   }

   public boolean isFriends(User user, User user2) {
      friendShipRepository.findAllByUserAndUser2(user, user2).orElseThrow(() -> new RuntimeException("You are not friends"));
      return true;
   }
}
