package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.user.friend_ship.friend.FriendDto;
import pl.user.friend_ship.invitation.InvitationDto;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/friend")
public class FriendShipResource {

    private FriendShipController friendShipController;

    @PostMapping("/add/{invitationId}")
    public ResponseEntity<FriendShipDto> addFriend(Principal principal, @PathVariable Long invitationId) {
        return ResponseEntity.ok(friendShipController.add(principal, invitationId));
    }

    @GetMapping("s")
    public ResponseEntity<List<FriendDto>> getFriend(Principal principal) {
        return ResponseEntity.ok(friendShipController.getFriends(principal));
    }

    @DeleteMapping("/remove/{friendShipId}")
    public ResponseEntity removeFriend(Principal principal, @PathVariable Long friendShipId) {
        friendShipController.remove(principal, friendShipId);
        return ResponseEntity.noContent().build();
    }


}
