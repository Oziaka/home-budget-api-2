package pl.user.friend_ship.invitation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.user.User;
import pl.user.UserDto;
import pl.user.UserProvider;
import pl.user.UserRandomTool;
import pl.user.friend_ship.FriendShip;
import pl.user.friend_ship.FriendShipDto;
import pl.user.friend_ship.FriendShipProvider;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InvitationResourceTest {

   private InvitationResource invitationResource;
   private InvitationService invitationService;
   private InvitationRepository invitationRepository;
   private UserProvider userProvider;
   private FriendShipProvider friendShipProvider;

   @BeforeEach
   void init() {
      invitationRepository = Mockito.mock(InvitationRepository.class);
      userProvider = Mockito.mock(UserProvider.class);
      friendShipProvider = Mockito.mock(FriendShipProvider.class);
      invitationService = new InvitationService(invitationRepository, userProvider, friendShipProvider);
      invitationResource = new InvitationResource(invitationService);
   }

   @Test
   void inviteUserReturnInvitationDtoWhenInvitedUserDoNotSendInviteUserEarlier() {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      UserDto invitedDto = UserRandomTool.randomUserDto();
      // when
      when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
      when(invitationRepository.findAllByInviterAndInvited(any(), any())).thenReturn(Optional.empty());
      when(friendShipProvider.couldFriendshipExist(any(), any())).thenReturn(false);
      when(invitationRepository.save(any())).thenAnswer(invocation -> {
         Invitation invitation = invocation.getArgument(0, Invitation.class);
         invitation.setId(1L);
         return invitation;
      });
      ResponseEntity<Object> invitationResponseEntity = invitationResource.inviteUser(userDto::getEmail, invitedDto.getEmail());
      // then
      InvitationDto expectedInvitationDto = InvitationDto.builder().id(1L).inviter(UserDto.builder().email(userDto.getEmail()).build()).invited(UserDto.builder().email(invitedDto.getEmail()).build()).build();
      assertEquals(HttpStatus.CREATED, invitationResponseEntity.getStatusCode());
      assertEquals(expectedInvitationDto, invitationResponseEntity.getBody());
   }

   @Test
   void InviteUserReturnFriendShipDtoWhenInvitedAlreadySendInvitationToInviter() throws InterruptedException {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      UserDto invitedDto = UserRandomTool.randomUserDto();
      // when
      when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
      when(invitationRepository.findAllByInviterAndInvited(any(), any())).thenReturn(Optional.empty(), Optional.of(new Invitation()));
      when(friendShipProvider.couldFriendshipExist(any(), any())).thenReturn(false);
      when(friendShipProvider.save(any())).thenAnswer(invocation -> {
         long i = 1;
         FriendShip friendShip = invocation.getArgument(0, FriendShip.class);
         friendShip.setId(i++);
         return friendShip;
      });
      LocalDateTime timeBeforeTest = LocalDateTime.now();
      ResponseEntity<Object> friendShipDtoResponseEntity = invitationResource.inviteUser(userDto::getEmail, invitedDto.getEmail());
      // then
      FriendShipDto expectedFriendShipDto = FriendShipDto.builder().id(1L).user(UserDto.builder().email(userDto.getEmail()).build()).user2(UserDto.builder().email(invitedDto.getEmail()).build()).build();
      assertEquals(HttpStatus.OK, friendShipDtoResponseEntity.getStatusCode());
      assertThat(friendShipDtoResponseEntity.getBody()).isEqualToIgnoringNullFields(expectedFriendShipDto);
      assertThat(((FriendShipDto) friendShipDtoResponseEntity.getBody()).getDateOfAdding()).isBetween(timeBeforeTest, LocalDateTime.now());
   }

   @Test
   void cancelInvitationReturnOnlyHttpStatusWhenUserIsInvited() {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      UserDto invitedDto = UserRandomTool.randomUserDto();
      Invitation invitation = InvitationRandomTool.randomInvitation(userDto, invitedDto);
      // when
      when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
      when(invitationRepository.findByInvitedAndId(any(), any())).thenReturn(Optional.of(invitation));
      ResponseEntity invitationResponseEntity = invitationResource.cancelInvitation(invitedDto::getEmail, invitation.getId());
      // then
      assertEquals(HttpStatus.NO_CONTENT, invitationResponseEntity.getStatusCode());
      assertNull(invitationResponseEntity.getBody());
   }

   @Test
   void removeInvitationReturnOnlyHttpStatusWhenUserIsInviter() {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      UserDto invitedDto = UserRandomTool.randomUserDto();
      Invitation invitation = InvitationRandomTool.randomInvitation(userDto, invitedDto);
      // when
      when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
      when(invitationRepository.findByInviterAndId(any(), any())).thenReturn(Optional.of(invitation));
      ResponseEntity invitationResponseEntity = invitationResource.removeInvitation(userDto::getEmail, invitation.getId());
      // then
      assertEquals(HttpStatus.NO_CONTENT, invitationResponseEntity.getStatusCode());
      assertNull(invitationResponseEntity.getBody());
   }

   @Test
   void getInvitationsFromUserReturnUserInvitations() {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      List<Invitation> userInvitations = Stream.iterate(0, i -> i + 1).limit(10).map(i -> InvitationRandomTool.randomInvitation(userDto, UserRandomTool.randomUserDto())).collect(Collectors.toList());
      // when
      when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
      when(invitationRepository.findAllByInviter(any())).thenReturn(userInvitations);
      ResponseEntity invitationResponseEntity = invitationResource.getInvitationsFromUser(userDto::getEmail);
      // then
      List<InvitationDto> expectedUserInvitations = userInvitations.stream().map(InvitationMapper::toDto).collect(Collectors.toList());
      assertEquals(HttpStatus.OK, invitationResponseEntity.getStatusCode());
      assertEquals(expectedUserInvitations, invitationResponseEntity.getBody());
   }

   @Test
   void getInvitationsToUserReturnInvitationsToUser() {
      // given
      UserDto userDto = UserRandomTool.randomUserDto();
      List<Invitation> userInvitations = Stream.iterate(0, i -> i + 1).limit(10).map(i -> InvitationRandomTool.randomInvitation(UserRandomTool.randomUserDto(), userDto)).collect(Collectors.toList());
      // when
      when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
      when(invitationRepository.findAllByInvited(any())).thenReturn(userInvitations);
      ResponseEntity invitationResponseEntity = invitationResource.getInvitationsToUser(userDto::getEmail);
      // then
      List<InvitationDto> expectedNotificationToUser = userInvitations.stream().map(InvitationMapper::toDto).collect(Collectors.toList());
      assertEquals(HttpStatus.OK, invitationResponseEntity.getStatusCode());
      assertEquals(expectedNotificationToUser, invitationResponseEntity.getBody());
   }
}