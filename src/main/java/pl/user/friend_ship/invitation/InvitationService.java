package pl.user.friend_ship.invitation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.user.UserService;
import pl.user.friend_ship.FriendShip;
import pl.user.friend_ship.FriendShipMapper;
import pl.user.friend_ship.FriendShipProvider;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvitationService {

   private InvitationRepository invitationRepository;
   private UserService userService;
   private FriendShipProvider friendShipProvider;

   Object invite(Principal principal, String invitedUserEmail) {
      User inviter = userService.getUser(principal);
      User invited = userService.getUser(invitedUserEmail::toString);
      checkCouldUserDoNotAddYourself(inviter, invited);
      Invitation invitation = Invitation.builder().inviter(inviter).invited(invited).build();
      checkCouldUserAlreadyInvite(inviter, invited);
      checkCouldUserDoNotInviteAlreadyFriend(inviter, invited);
//        TODO Notification to invited user
      if (checkCouldInvitedSendInviteEarly(invited, inviter)) {
         this.remove(invited, inviter);
         FriendShip friendShip = friendShipProvider.save(FriendShip.builder().user(inviter).user2(invited).dateOfAdding(LocalDateTime.now()).build());
         friendShipProvider.save(FriendShip.builder().user(invited).user2(inviter).dateOfAdding(LocalDateTime.now()).build());
         return FriendShipMapper.toDto(friendShip);
      } else {
         Invitation savedInvitation = this.save(invitation);
         return InvitationMapper.toDto(savedInvitation);
      }
   }

   private boolean checkCouldInvitedSendInviteEarly(User inviter, User invited) {
      return this.isExist(invited, inviter);
   }


   private void checkCouldUserAlreadyInvite(User inviter, User invited) {
      if (this.isExist(inviter, invited)) throw new RuntimeException("Can only send one invite to another user");
   }

   private void checkCouldUserDoNotInviteAlreadyFriend(User inviter, User invited) {
      if (friendShipProvider.couldFriendshipExist(inviter, invited))
         throw new RuntimeException("Can not invite already friend");
   }

   private void checkCouldUserDoNotAddYourself(User inviter, User invited) {
      if (invited.equals(inviter)) throw new RuntimeException("Can not add yourself to friend");
   }

   void removeInvitation(Principal principal, Long invitationId) {
      User inviter = userService.getUser(principal);
      Invitation invitation = this.getOneByInviter(inviter, invitationId);
      this.remove(invitation);
   }

   void cancelInvitation(Principal principal, Long invitationId) {
      User invited = userService.getUser(principal);
      Invitation invitation = this.getOneByInvited(invited, invitationId);
      this.remove(invitation);
//        TODO notification to inviter
   }

   List<InvitationDto> getInvitationsFromUser(Principal principal) {
      User user = userService.getUser(principal);
      List<Invitation> allByInviter = this.getAllByInviter(user);
      return allByInviter.stream().map(InvitationMapper::toDto).collect(Collectors.toList());
   }

   List<InvitationDto> getInvitationsToUser(Principal principal) {
      User user = userService.getUser(principal);
      List<Invitation> allByInviter = this.getAllByInviter(user);
      return allByInviter.stream().map(InvitationMapper::toDto).collect(Collectors.toList());
   }

   public Invitation save(Invitation invitation) {
      return invitationRepository.save(invitation);
   }

   public boolean isExist(User inviter, User invited) {
      return invitationRepository.findAllByInviterAndInvited(inviter, invited).isPresent();
   }

   public void remove(User inviter, User invited) {
      invitationRepository.removeByInviterAndInvited(inviter, invited);
   }

   public void remove(Invitation invitation) {
      invitationRepository.delete(invitation);
   }

   public Invitation getOneByInviter(User inviter, Long invitationId) {
      return invitationRepository.findByInviterAndId(inviter, invitationId).orElseThrow(ThereIsNoYourPropertyException::new);
   }

   public Invitation getOneByInvited(User invited, Long invitationId) {
      return invitationRepository.findByInviterAndId(invited, invitationId).orElseThrow(ThereIsNoYourPropertyException::new);
   }

   public List<Invitation> getAllByInviter(User inviter) {
      return invitationRepository.findAllByInviter(inviter);
   }

   public List<Invitation> getAllByInvited(User invited) {
      return invitationRepository.findAllByInvited(invited);
   }
}

