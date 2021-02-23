package pl.user.friend_ship;

import pl.user.User;
import pl.user.UserDto;
import pl.user.friend_ship.FriendShip;

import java.time.LocalDateTime;

public class FriendShipRandomTool {
  public static FriendShip randomFriendShip(UserDto userDto, UserDto friendDto) {
    return FriendShip.builder().user(User.builder().email(friendDto.getEmail()).build()).user2(User.builder().email(userDto.getEmail()).build()).dateOfAdding(LocalDateTime.now()
    ).build();
  }
}
