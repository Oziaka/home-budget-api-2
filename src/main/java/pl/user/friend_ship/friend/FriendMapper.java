package pl.user.friend_ship.friend;

import pl.user.UserMapper;
import pl.user.friend_ship.FriendShip;

public class FriendMapper {
  public static FriendDto toDto(FriendShip friendShip) {
    return FriendDto.builder().friend(UserMapper.toDto(friendShip.getUser2())).dateOfAdding(friendShip.getDateOfAdding()).friendShipId(friendShip.getId()).build();
  }
}
