package pl.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import pl.security.user_role.UserRoleService;
import pl.wallet.Wallet;
import pl.wallet.WalletService;
import pl.wallet.category.CategoryService;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class UserController {

  private UserService userService;
  private UserRoleService userRoleService;
  private PasswordEncoder passwordEncoder;
  private WalletService walletService;
  private CategoryService categoryService;

  UserDto addUserWithDefaultsResources (UserDto userDto) {
    User user = UserMapper.toEntity(userDto);
    encodePassword(user);
    addDefaultRoles(user);
    User savedUser = userService.save(user);
    addDefaultCategories(user);
    Wallet wallet = walletService.saveDefaultWallet(user);
    return UserMapper.toDto(userService.setFavoriteWallet(savedUser, wallet.getId()));
  }

  private void addDefaultCategories (User user) {
    categoryService.getDefaultCategories().forEach(user::addCategory);
  }

  private void addDefaultRoles (User user) {
    userRoleService.findDefaultRoles().forEach(user::addRole);
  }

  private void encodePassword (User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
  }

  UserDto getUserByPrincipal (Principal principal) {
    return UserMapper.toDto(userService.getUser(principal));
  }

  UserDto setFavoriteWallet (Principal principal, Long walletId) {
    User user = userService.getUser(principal);
    return UserMapper.toDto(userService.setFavoriteWallet(user, walletId));
  }


}
