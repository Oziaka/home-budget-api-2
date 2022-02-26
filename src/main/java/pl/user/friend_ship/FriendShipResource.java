package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.user.friend_ship.friend.FriendDto;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/friend")
public class FriendShipResource {

    private FriendShipService friendShipService;

    @PutMapping("/add/{invitationId}")
    public ResponseEntity<FriendShipDto> addFriend(Principal principal, @PathVariable Long invitationId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(friendShipService.add(principal, invitationId));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FriendDto>> getFriends(Principal principal) {
        return ResponseEntity.ok(friendShipService.getFriends(principal));
    }

    @DeleteMapping("/remove/{friendShipId}")
    public ResponseEntity removeFriend(Principal principal, @PathVariable Long friendShipId) {
        friendShipService.remove(principal, friendShipId);
        return ResponseEntity.noContent().build();
    }


}
