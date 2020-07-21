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

    @PostMapping("/invite")
    public ResponseEntity<Object> inviteUser(Principal principal, @RequestBody String invitedUserEmail) {
        return ResponseEntity.ok(friendShipController.invite(principal, invitedUserEmail));
    }

    @PostMapping("/add/{invitationId}")
    public ResponseEntity<FriendShipDto> addFriend(Principal principal, @PathVariable Long invitationId) {
        return ResponseEntity.ok(friendShipController.add(principal,invitationId));
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

    @DeleteMapping("/invitation/cancel/{invitationId}")
    public void cancelInvitation(Principal principal, @PathVariable Long invitationId) {
        friendShipController.cancelInvitation(principal, invitationId);
    }

    @DeleteMapping("/invitation/remove{invitationId}")
    public void removeInvitation(Principal principal, @PathVariable Long invitationId) {
        friendShipController.removeInvitation(principal, invitationId);
    }

    @GetMapping("invitation/from_user")
    public ResponseEntity<List<InvitationDto>> getInvitationsFromUser(Principal principal) {
        return ResponseEntity.ok(friendShipController.getInvitationsFromUser(principal));
    }

    @GetMapping("invitation/to_user")
    public ResponseEntity<List<InvitationDto>> getInvitationsToUser(Principal principal) {
        return ResponseEntity.ok(friendShipController.getInvitationsToUser(principal));
    }
}
