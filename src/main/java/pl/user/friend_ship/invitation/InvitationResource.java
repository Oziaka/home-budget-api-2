package pl.user.friend_ship.invitation;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class InvitationResource {

   private InvitationController invitationController;

   @PostMapping("/invite")
   public ResponseEntity<Object> inviteUser(Principal principal, @RequestBody String invitedUserEmail) {
      return ResponseEntity.ok(invitationController.invite(principal, invitedUserEmail));
   }

   @DeleteMapping("/invitation/cancel/{invitationId}")
   public void cancelInvitation(Principal principal, @PathVariable Long invitationId) {
      invitationController.cancelInvitation(principal, invitationId);
   }

   @DeleteMapping("/invitation/remove{invitationId}")
   public void removeInvitation(Principal principal, @PathVariable Long invitationId) {
      invitationController.removeInvitation(principal, invitationId);
   }

   @GetMapping("invitation/from_user")
   public ResponseEntity<List<InvitationDto>> getInvitationsFromUser(Principal principal) {
      return ResponseEntity.ok(invitationController.getInvitationsFromUser(principal));
   }

   @GetMapping("invitation/to_user")
   public ResponseEntity<List<InvitationDto>> getInvitationsToUser(Principal principal) {
      return ResponseEntity.ok(invitationController.getInvitationsToUser(principal));
   }
}
