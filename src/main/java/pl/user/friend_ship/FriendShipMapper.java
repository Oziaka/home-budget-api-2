package pl.user.friend_ship;

import pl.user.UserMapper;

public class FriendShipMapper {
   public static FriendShipDto toDto(FriendShip friendShip) {
      return FriendShipDto.builder()
         .id(friendShip.getId())
         .user(UserMapper.toDto(friendShip.getUser()))
         .user2(UserMapper.toDto(friendShip.getUser2()))
         .dateOfAdding(friendShip.getDateOfAdding())
         .build();
   }
}
