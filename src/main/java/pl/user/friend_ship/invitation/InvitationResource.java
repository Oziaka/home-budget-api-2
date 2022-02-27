package pl.user.friend_ship.invitation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class InvitationResource {

    private InvitationService invitationService;

    @PostMapping("/invite")
    public ResponseEntity<Object> inviteUser(Principal principal, @RequestBody String invitedUserEmail) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invitationService.invite(principal, invitedUserEmail.replace("\"", "")));
    }

    @DeleteMapping("/invitation/cancel/{invitationId}")
    public ResponseEntity<?> cancelInvitation(Principal principal, @PathVariable Long invitationId) {
        invitationService.cancelInvitation(principal, invitationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/invitation/remove/{invitationId}")
    public ResponseEntity<?> removeInvitation(Principal principal, @PathVariable Long invitationId) {
        invitationService.removeInvitation(principal, invitationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("invitation/from_user")
    public ResponseEntity<List<InvitationDto>> getInvitationsFromUser(Principal principal) {
        return ResponseEntity.ok(invitationService.getInvitationsFromUser(principal));
    }

    @GetMapping("invitation/to_user")
    public ResponseEntity<List<InvitationDto>> getInvitationsToUser(Principal principal) {
        return ResponseEntity.ok(invitationService.getInvitationsToUser(principal));
    }
}
