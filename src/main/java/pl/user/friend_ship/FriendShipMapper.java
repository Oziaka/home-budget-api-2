package pl.user.friend_ship;

public class FriendShipMapper {
    public static FriendShipDto toDto(FriendShip friendShip) {
        return FriendShipDto.builder()
                .id(friendShip.getId())
                .user(friendShip.getUser())
                .user2(friendShip.getUser2())
                .dateOfAdding(friendShip.getDateOfAdding())
                .build();
    }
}
