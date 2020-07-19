package pl.user.friend_ship;

import lombok.Builder;
import lombok.Getter;
import pl.user.User;

import java.time.LocalDateTime;

@Getter
public class FriendShipDto {

    private Long id;
    private User user;
    private User user2;
    private LocalDateTime dateOfAdding;

    @Builder
    public FriendShipDto(Long id, User user, User user2, LocalDateTime dateOfAdding) {
        this.id = id;
        this.user = user;
        this.user2 = user2;
        this.dateOfAdding = dateOfAdding;
    }
}
