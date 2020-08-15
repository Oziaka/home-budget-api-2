package pl.wallet;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/wallet")
@AllArgsConstructor
@CrossOrigin("${cors.allowed-origins}")
public class WalletResource {
   private WalletController walletController;

   @PutMapping("/add")
   public ResponseEntity<WalletDto> addWallet(Principal principal, @Valid @RequestBody WalletDto walletDto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(walletController.addWallet(principal, walletDto));
   }

   @GetMapping("s")
   public ResponseEntity<List<WalletDto>> getWallets(Principal principal) {
      return ResponseEntity.ok(walletController.getWallets(principal));
   }

   @PostMapping("{/walletId}/edit")
   public ResponseEntity<WalletDto> editWallet(Principal principal, Long walletId, @Valid @RequestBody WalletDto walletDto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(walletController.editWallet(principal,walletId, walletDto));
   }

   @DeleteMapping("/{walletId}/remove")
   public ResponseEntity removeWallet(Principal principal, @PathVariable Long walletId) {
      walletController.removeWallet(principal, walletId);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("/{walletId}")
   public ResponseEntity<WalletDto> getWallet(Principal principal, @PathVariable Long walletId) {
      return ResponseEntity.ok(walletController.getWallet(principal, walletId));
   }

   @PostMapping("/{walletId}/add_friend_to_wallet")
   public ResponseEntity<WalletDto> shareWalletWithFriend(Principal principal, @PathVariable Long walletId, @RequestBody String friendUserEmail) {
      return ResponseEntity.ok(walletController.shareWalletWithFriend(principal, walletId, friendUserEmail));
   }

   @PostMapping("/{walletId}/change_owner")
   public ResponseEntity<WalletDto> changeWalletOwner(Principal principal, @PathVariable Long walletId, @RequestBody String newOwnerUserEmail) {
      return ResponseEntity.ok(walletController.changeWalletOwner(principal, walletId, newOwnerUserEmail));
   }

   @PostMapping("/{walletId}/remove_friend_from_wallet")
   public ResponseEntity<WalletDto> removeFriendFromWallet(Principal principal, @PathVariable Long walletId, @RequestBody String friendToRemoveFromWalletUserEmail) {
      return ResponseEntity.ok(walletController.removeUserFromWallet(principal, walletId, friendToRemoveFromWalletUserEmail));
   }
}
