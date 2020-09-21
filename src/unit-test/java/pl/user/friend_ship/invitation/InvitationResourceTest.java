package pl.user.friend_ship.invitation;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import pl.user.UserService;
import pl.user.friend_ship.FriendShipProvider;

class InvitationResourceTest {

   private InvitationResource invitationResource;
   private InvitationService invitationService;
   private InvitationRepository invitationRepository;
   private UserService userService;
   private FriendShipProvider friendShipProvider;

   @BeforeEach
   void init() {
      InvitationResource invitationResource = new InvitationResource(invitationService);
      InvitationService invitationService = new InvitationService(invitationRepository, userService, friendShipProvider);
      InvitationRepository invitationRepository = Mockito.mock(InvitationRepository.class);
      UserService userService = Mockito.mock(UserService.class);
      FriendShipProvider friendShipProvider = Mockito.mock(FriendShipProvider.class);
   }
}