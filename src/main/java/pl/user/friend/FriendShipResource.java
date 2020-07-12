package pl.user.friend;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.user.UserDto;

import java.security.Principal;

@RestController
@RequestMapping("/user/friend_ship")
@AllArgsConstructor
public class FriendShipResource {

  private FriendShipController friendShipController;


  @PostMapping("/invite")
  public ResponseEntity<FriendShipDto> invite (Principal principal, @RequestBody FriendShip friendShip) {
    return ResponseEntity.status(HttpStatus.CREATED).body(friendShipController.invite(principal));
  }
  @PostMapping("/add")
  public ResponseEntity<FriendShipDto> invite (Principal principal, @RequestBody FriendShip friendShip) {
    return ResponseEntity.status(HttpStatus.CREATED).body(friendShipController.addFriend(principal));
  }
//
//  @PostMapping("/{walletId}/add")
//  public ResponseEntity<WalletDto> shareWalletWithFriend (Principal principal, @PathVariable Long walletId, @RequestBody UserDto friend) {
//    return ResponseEntity.ok(friendController.shareWalletWithFriend(principal, friend, walletId));
//  }
//
//  @DeleteMapping("/{walletId}/remove")
//  public ResponseEntity<WalletDto> removeFriendFromWallet (Principal principal, @PathVariable Long wallteId, @RequestBody UserDto friend) {
//    return ResponseEntity.status(HttpStatus.OK).body(friendController.removeFriendFromWallet(principal, wallteId, friend));
//  }
}
