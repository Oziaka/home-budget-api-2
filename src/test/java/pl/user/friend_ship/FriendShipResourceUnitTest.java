package pl.user.friend_ship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.user.UserRandomTool;
import pl.user.User;
import pl.user.UserDto;
import pl.user.UserProvider;
import pl.user.friend_ship.friend.FriendDto;
import pl.user.friend_ship.friend.FriendMapper;
import pl.user.friend_ship.invitation.Invitation;
import pl.user.friend_ship.invitation.InvitationProvider;
import pl.user.friend_ship.invitation.InvitationRandomTool;
import pl.user.friend_ship.invitation.InvitationService;

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

class FriendShipResourceUnitTest {

  private FriendShipRepository friendShipRepository;
  private InvitationProvider invitationProvider;
  private UserProvider userProvider;
  private FriendShipService friendShipService;
  private FriendShipResource friendShipResource;

  @BeforeEach
  void init() {
    friendShipRepository = Mockito.mock(FriendShipRepository.class);
    invitationProvider = Mockito.mock(InvitationProvider.class);
    userProvider = Mockito.mock(UserProvider.class);
    friendShipService = new FriendShipService(invitationProvider, userProvider, friendShipRepository);
    friendShipResource = new FriendShipResource(friendShipService);
  }

  @Test
  void addFriendReturnFriendShipDto() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    UserDto friendDto = UserRandomTool.randomUserDto();
    Invitation invitation = InvitationRandomTool.randomInvitation(userDto, friendDto);
    // when
    when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
    when(invitationProvider.getOneByInvited(any(), any())).thenReturn(invitation);
    when(friendShipRepository.save(any())).thenAnswer(invocation -> {
      FriendShip friendShip = invocation.getArgument(0, FriendShip.class);
      friendShip.setId(1L);
      return friendShip;
    });
    LocalDateTime timeBeforeGetResponse = LocalDateTime.now();
    ResponseEntity friendShipDtoResponseEntity = friendShipResource.addFriend(userDto::getEmail, invitation.getId());
    // then
    FriendShipDto expectedFriendShipDto = FriendShipDto.builder().id(1L).user(UserDto.builder().email(friendDto.getEmail()).build()).user2(UserDto.builder().email(userDto.getEmail()).build()).build();
    assertEquals(HttpStatus.CREATED, friendShipDtoResponseEntity.getStatusCode());
    assertThat(friendShipDtoResponseEntity.getBody()).isEqualToIgnoringNullFields(expectedFriendShipDto);
    assertThat(((FriendShipDto) friendShipDtoResponseEntity.getBody()).getDateOfAdding()).isStrictlyBetween(timeBeforeGetResponse, LocalDateTime.now());
  }

  @Test
  void getFriendsReturnUserFriends() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    List<FriendShip> friends = Stream.iterate(1, i -> i + 1).limit(10).map(i -> {
      FriendShip friendShip = FriendShipRandomTool.randomFriendShip(userDto, UserRandomTool.randomUserDto());
      friendShip.setId(Long.valueOf(i));
      return friendShip;
    }).collect(Collectors.toList());
    // when
    when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
    when(friendShipRepository.findALlByUser(any())).thenReturn(friends);
    ResponseEntity<List<FriendDto>> userFriends = friendShipResource.getFriends(userDto::getEmail);
    // then
    List<FriendDto> expectedFriendDto = friends.stream().map(FriendMapper::toDto).collect(Collectors.toList());
    assertEquals(HttpStatus.OK, userFriends.getStatusCode());
    assertEquals(expectedFriendDto, userFriends.getBody());
  }

  @Test
  void removeFriendsReturnHttpStatusNoContentWhenUserCanRemoveFriend() {
    // given
    UserDto userDto = UserRandomTool.randomUserDto();
    UserDto friendDto = UserRandomTool.randomUserDto();
    FriendShip friendShip = FriendShipRandomTool.randomFriendShip(userDto, UserRandomTool.randomUserDto());
    // when
    when(userProvider.get(any())).thenAnswer(invocation -> User.builder().email(invocation.getArgument(0, Principal.class).getName()).build());
    when(friendShipRepository.findByUserAndId(any(), any())).thenReturn(Optional.of(new FriendShip()));
    when(friendShipRepository.findAllByUserAndUser2(any(), any())).thenReturn(Optional.of(new FriendShip()));
    ResponseEntity friendShipDtoResponseEntity = friendShipResource.removeFriend(userDto::getEmail, friendShip.getId());
    // then
    assertEquals(HttpStatus.NO_CONTENT, friendShipDtoResponseEntity.getStatusCode());
    assertNull(friendShipDtoResponseEntity.getBody());
  }
}