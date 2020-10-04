package pl.user.friend_ship;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.user.User;
import pl.user.UserDto;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class FriendShipDto {

   private Long id;
   private UserDto user;
   private UserDto user2;
   private LocalDateTime dateOfAdding;

   @Builder
   public FriendShipDto(Long id, UserDto user, UserDto user2, LocalDateTime dateOfAdding) {
      this.id = id;
      this.user = user;
      this.user2 = user2;
      this.dateOfAdding = dateOfAdding;
   }
}
