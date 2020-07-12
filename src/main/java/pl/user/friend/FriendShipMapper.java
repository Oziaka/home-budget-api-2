package pl.user.friend;

public class FriendShipMapper {

  public static FriendShipDto toDto (FriendShip savedFriendShip) {
    return FriendShipDto.builder().dateOfMakingFriend(savedFriendShip.getDateOfMakingFiends())
      .friendName(savedFriendShip.user.getUserName())
      .build();
  }
//  public static FriendDto toDto (Friend friend) {
//    return FriendDto.builder()
//      .name(friend.getFriend().getEmail())
//      .dateOfMakingFriend(friend.getDateOfMakingFiends())
//      .build();
//  }
}
