package pl.user.friend_ship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.user.UserService;
import pl.user.friend_ship.invitation.InvitationService;

class FriendShipResourceTest {

   private FriendShipRepository friendShipRepository;
   private InvitationService invitationService;
   private UserService userService;
   private FriendShipService friendShipService;
   private FriendShipResource friendShipResource;

   @BeforeEach
   void init() {
      FriendShipRepository friendShipRepository = Mockito.mock(FriendShipRepository.class);
      InvitationService invitationService = Mockito.mock(InvitationService.class);
      UserService userService = Mockito.mock(UserService.class);
      friendShipService = new FriendShipService(invitationService, userService, friendShipRepository);
      friendShipResource = new FriendShipResource(friendShipService);
   }

   @Test
   void addFriendReturnFriendShipDto() {
      // given
//      Invitation invitation = InvitationRandomTool.

      // when


      // then
   }

}