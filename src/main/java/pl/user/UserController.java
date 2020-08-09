package pl.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import pl.exception.UserMustHaveUniqueEmailException;
import pl.security.user_role.UserRoleService;
import pl.user.item_key.UserItemKey;
import pl.user.item_key.UserItemKeyService;
import pl.user.user_notification.notification.notification.NotificationService;
import pl.wallet.Wallet;
import pl.wallet.WalletService;
import pl.wallet.category.CategoryService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;

@Controller
@AllArgsConstructor
public class UserController {

   private static final String DEFAULT_WALLET_NAME = "Wallet";

   private UserService userService;
   private UserRoleService userRoleService;
   private PasswordEncoder passwordEncoder;
   private WalletService walletService;
   private CategoryService categoryService;
   private UserItemKeyService userItemKeyService;
   private NotificationService notificationService;


   UserDto addUserWithDefaultsResources(UserDto userDto) {
      User user = UserMapper.toEntity(userDto);
      encodePassword(user);
      addDefaultRoles(user);
      user = userService.save(user);
      addDefaultCategories(user);
      saveDefaultWallet(user);
      User savedUser = userService.save(user);
      return UserMapper.toDtoForSelf(savedUser);
   }

   private Wallet saveDefaultWallet(User user) {
      Wallet defaultWallet = createDefaultWallet();
      defaultWallet.setOwner(user);
      defaultWallet.setUsers(Collections.singleton(user));
      user.addWallet(defaultWallet);
      return walletService.save(defaultWallet);
   }

   private Wallet createDefaultWallet() {
      Wallet wallet = new Wallet();
      wallet.setName(DEFAULT_WALLET_NAME);
      wallet.setBalance(BigDecimal.ZERO);
      return wallet;
   }

   private void addDefaultCategories(User user) {
      categoryService.getAllDefaults().forEach(user::addCategory);
   }

   private void addDefaultRoles(User user) {
      userRoleService.getDefaults().forEach(user::addRole);
   }

   private void encodePassword(User user) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
   }

   UserDto getProfile(Principal principal) {
      return UserMapper.toDtoForSelf(userService.get(principal));
   }

   UserDto editUser(Principal principal, UserDto userDto) {
      if (!isUserHasUniqueEmail(userDto.getEmail()))
         throw new UserMustHaveUniqueEmailException();
      User user = userService.get(principal);
      User updatedUser = updateUserFromNotNullFieldsInUserDto(user, userDto);
      User savedUser = userService.save(updatedUser);
      return UserMapper.toDtoForSelf(savedUser);
   }

   private User updateUserFromNotNullFieldsInUserDto(User user, UserDto userDto) {
      User userToUpdate = user;
      if (userDto.getUserName() != null)
         userToUpdate.setUserName(userDto.getUserName());
      if (userDto.getEmail() != null)
         userToUpdate.setEmail(userDto.getEmail());
      if (userDto.getPassword() != null)
         userToUpdate.setPassword(userDto.getPassword());
      if (userDto.getItems() != null) {
         userDto.getItems()
            .entrySet().stream().filter(item -> userItemKeyService.getAll().stream()
            .map(UserItemKey::getName).anyMatch(itemKey -> itemKey.equals(item.getKey())))
            .forEach(item -> userToUpdate.addItem(item.getKey(), item.getValue()));
      }
      return userToUpdate;
   }

   private Boolean isUserHasUniqueEmail(String email) {
      return !userService.emailIsUsed(email);
   }

}


