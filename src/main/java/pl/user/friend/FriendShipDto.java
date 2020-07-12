package pl.user.friend;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class FriendShipDto {

  private String friendName;

  private LocalDateTime dateOfMakingFriend;

  @Builder
  public FriendShipDto (String friendName, LocalDateTime dateOfMakingFriend) {
    this.friendName = friendName;
    this.dateOfMakingFriend = dateOfMakingFriend;
  }
}
