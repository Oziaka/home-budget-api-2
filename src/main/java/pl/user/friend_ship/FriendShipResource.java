package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.user.friend_ship.friend.FriendDto;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/friend")
public class FriendShipResource {

    private FriendShipController friendShipController;

    @PostMapping("/invite")
    public ResponseEntity<Object> inviteUser(Principal principal, @RequestBody String invitedUserEmail) {
        return ResponseEntity.ok(friendShipController.invite(principal, invitedUserEmail));
    }

    @PostMapping("/add")
    public ResponseEntity<FriendShipDto> addFriend(Principal principal, @RequestParam String key) {
        return ResponseEntity.ok(friendShipController.add(principal, key));
    }

    @GetMapping
    public ResponseEntity<List<FriendDto>> getFriend(Principal principal) {
        return ResponseEntity.ok(friendShipController.getFriends(principal));
    }

    @DeleteMapping("/remove/{friendShipId}")
    public ResponseEntity removeFriend(Principal principal, @PathVariable Long friendShipId) {
        friendShipController.remove(principal, friendShipId);
        return ResponseEntity.noContent().build();
    }
}
