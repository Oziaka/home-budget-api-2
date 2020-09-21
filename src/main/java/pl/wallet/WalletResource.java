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
   private WalletService walletService;

   @PutMapping("/add")
   public ResponseEntity<WalletDto> addWallet(Principal principal, @Valid @RequestBody WalletDto walletDto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(walletService.addWallet(principal, walletDto));
   }

   @GetMapping("s")
   public ResponseEntity<List<WalletDto>> getWallets(Principal principal) {
      return ResponseEntity.ok(walletService.getWallets(principal));
   }

   @PostMapping("{/walletId}/edit")
   public ResponseEntity<WalletDto> editWallet(Principal principal, Long walletId, @Valid @RequestBody WalletDto walletDto) {
      return ResponseEntity.ok(walletService.editWallet(principal, walletId, walletDto));
   }

   @DeleteMapping("/{walletId}/remove")
   public ResponseEntity removeWallet(Principal principal, @PathVariable Long walletId) {
      walletService.removeWallet(principal, walletId);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("/{walletId}")
   public ResponseEntity<WalletDto> getWallet(Principal principal, @PathVariable Long walletId) {
      return ResponseEntity.ok(walletService.getWallet(principal, walletId));
   }

   @PostMapping("/{walletId}/add_friend_to_wallet")
   public ResponseEntity<WalletDto> shareWalletWithFriend(Principal principal, @PathVariable Long walletId, @RequestBody String friendUserEmail) {
      return ResponseEntity.ok(walletService.shareWalletWithFriend(principal, walletId, friendUserEmail));
   }

   @PostMapping("/{walletId}/change_owner")
   public ResponseEntity<WalletDto> changeWalletOwner(Principal principal, @PathVariable Long walletId, @RequestBody String newOwnerUserEmail) {
      return ResponseEntity.ok(walletService.changeWalletOwner(principal, walletId, newOwnerUserEmail));
   }

   @PostMapping("/{walletId}/remove_friend_from_wallet")
   public ResponseEntity<WalletDto> removeFriendFromWallet(Principal principal, @PathVariable Long walletId, @RequestBody String friendToRemoveFromWalletUserEmail) {
      return ResponseEntity.ok(walletService.removeUserFromWallet(principal, walletId, friendToRemoveFromWalletUserEmail));
   }
}
