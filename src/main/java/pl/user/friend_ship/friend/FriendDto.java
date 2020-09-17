package pl.user.friend_ship.friend;

import lombok.Builder;
import lombok.Getter;
import pl.user.UserDto;

import java.time.LocalDateTime;

@Getter
public class FriendDto {

   private UserDto friend;
   private LocalDateTime dateOfAdding;
   private Long friendShipId;
   @Builder
   public FriendDto(UserDto friend, LocalDateTime dateOfAdding, Long friendShipId) {
      this.friend = friend;
      this.dateOfAdding = dateOfAdding;
      this.friendShipId = friendShipId;
   }
}
