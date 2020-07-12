package pl.user.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.user.User;
import pl.user.UserDto;
import pl.user.UserService;
import pl.wallet.WalletService;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class FriendShipController {

  private FriendShipService friendShipService;
  private UserService userService;
  private WalletService walletService;


  @Autowired
  public FriendShipController (WalletService walletService, FriendShipService friendShipService, UserService userService) {
    this.friendShipService = friendShipService;
    this.userService = userService;
    this.walletService = walletService;
  }

  FriendShipDto addFriend (Principal principal, UserDto friendDto) {
    User user = userService.getUser(principal);
    User friend = userService.getUser(friendDto::getEmail);
    friendShipService.areTheyAlreadyFriends(user, friend);
    friendShipService.save(FriendShip.builder().dateOfMakingFiends(LocalDateTime.now()).user(friend).user2(user).build());
    FriendShip savedFriendShip = friendShipService.save(FriendShip.builder().dateOfMakingFiends(LocalDateTime.now()).user(user).user2(friend).build());
    return FriendShipMapper.toDto(savedFriendShip);
  }

//  WalletDto shareWalletWithFriend (Principal principal, UserDto friendDto, Long walletId) {
//    User user = userService.getUser(principal);
//    User friend = userService.getUser(friendDto::getEmail);
//    try {
//      walletService.isUserWallet(friend, walletId);
//    } catch (ThereIsNoYourPropertyException e) {
//      walletService.isUserWallet(user, walletId);
//      return WalletMapper.toDto(walletService.addFriendToWallet(friend, walletId));
//    }
//    throw new WalletAlreadyIsPropertyOfYourFriend("This wallet already belongs to your friend");
//  }

//  WalletDto removeFriendFromWallet (Principal principal, Long walletId, UserDto friendDto) {
//    User user = userService.getUserByPrincipal(principal);
//    User friend = userService.getUserByEmail(friendDto.getEmail());
//
//    walletService.isUserWallet(friend, walletId);
//    walletService.isUserWallet(user, walletId);
//
//    walletService.removeFriendFromWallet(friend, walletId);
//
//    return WalletMapper.toDto(walletService.addFriendToWallet(friend, walletId));
//  }
}
