package pl.user.friend_ship;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.user.friend_ship.invitation.InvitationDto;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/friend")
public class FriendShipResource {

    private FriendShipController friendShipController;

    @PostMapping("/invite")
    public ResponseEntity<Object> invite(Principal principal, @RequestBody String invitedUserEmail) {
        return ResponseEntity.ok(friendShipController.invite(principal, invitedUserEmail));
    }

    @PostMapping("/add")
    public ResponseEntity<FriendShipDto> add(Principal principal, @RequestParam String key) {
        return ResponseEntity.ok(friendShipController.add(principal,key));
    }

}
