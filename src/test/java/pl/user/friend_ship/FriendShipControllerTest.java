package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.h2.engine.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.user.User;
import pl.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FriendShipControllerTest {
    private FriendShipService friendShipService;
    private UserService userService;

    @Autowired
    FriendShipControllerTest(FriendShipService friendShipService, UserService userService) {
        this.friendShipService = friendShipService;
        this.userService = userService;
    }

    @Test
    void eachUserCanAddAnotherUserToFriend() {
        User user1 = User.builder().email("admin@o2.pl").password("admin").build();
        User user2 = User.builder().email("oziaka@o2.pl").password("admin").build();

        user1 = userService.save(user1);
        user2 = userService.save(user2);

        FriendShip friendShip = FriendShip.builder().user2(user2).user(user1).dateOfAdding(LocalDateTime.now()).build();

        friendShipService.save(friendShip);

    }
}