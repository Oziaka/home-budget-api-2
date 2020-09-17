package pl.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.exception.DataNotFoundExeption;
import pl.security.user_role.UserRole;
import pl.security.user_role.UserRoleService;
import pl.user.item_key.UserItemKey;
import pl.user.item_key.UserItemKeyService;
import pl.wallet.Wallet;
import pl.wallet.WalletProvider;
import pl.wallet.category.CategoryService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;

@Service
@AllArgsConstructor
public class UserService {

   private static final String DEFAULT_WALLET_NAME = "Wallet";

   private UserRepository userRepository;
   private UserRoleService userRoleService;
   private PasswordEncoder passwordEncoder;
   private CategoryService categoryService;
   private UserItemKeyService userItemKeyService;
   private WalletProvider walletProvider;

   UserDto addUserWithDefaultsResources(UserDto userDto) {
      User user = UserMapper.toEntity(userDto);
      encodePassword(user);
      addDefaultRoles(user);
      user = this.saveUser(user);
      addDefaultCategories(user);
      addDefaultWallet(user);
      User savedUser = this.saveUser(user);
      return UserMapper.toDtoForSelf(savedUser);
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
      return UserMapper.toDtoForSelf(this.getUser(principal));
   }

   UserDto editUser(Principal principal, UserDto userDto) {
      if (!isUserHasUniqueEmail(userDto.getEmail()))
         throw new RuntimeException("User must have unique email");
      User user = this.getUser(principal);
      User updatedUser = updateUserFromNotNullFieldsInUserDto(user, userDto);
      User savedUser = this.saveUser(updatedUser);
      return UserMapper.toDtoForSelf(savedUser);
   }

   public UserDto grantPermission(Long userRoleId, String email) {
      User user = this.getUser(() -> email);
      UserRole userRole = userRoleService.getOne(userRoleId);
      user.addRole(userRole);
      User save = this.saveUser(user);
      return UserMapper.toDtoWithRoles(save);
   }

   public UserDto revokePermission(Long userRoleId, String email) {
      User user = this.getUser(() -> email);
      UserRole userRole = userRoleService.getOne(userRoleId);
      user.removeRole(userRole);
      User save = this.saveUser(user);
      return UserMapper.toDtoWithRoles(save);
   }

   private User updateUserFromNotNullFieldsInUserDto(User user, UserDto userDto) {
      if (userDto.getUserName() != null)
         user.setUserName(userDto.getUserName());
      if (userDto.getEmail() != null)
         user.setEmail(userDto.getEmail());
      if (userDto.getPassword() != null)
         user.setPassword(userDto.getPassword());
      if (userDto.getItems() != null) {
         userDto.getItems()
            .entrySet().stream().filter(item -> userItemKeyService.getAll().stream()
            .map(UserItemKey::getName).anyMatch(itemKey -> itemKey.equals(item.getKey())))
            .forEach(item -> user.addItem(item.getKey(), item.getValue()));
      }
      return user;
   }

   private Boolean isUserHasUniqueEmail(String email) {
      return !this.userRepository.findByEmail(email).isPresent();
   }

   private User getUser(String email) {
      return userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundExeption("User not found"));
   }

   public User getUser(Principal principal) {
      return getUser(principal.getName());
   }

   boolean emailIsUsed(String email) {
      return userRepository.findByEmail(email).isPresent();
   }

   public User saveUser(User user) {
      return userRepository.save(user);
   }

   private Wallet createDefaultWallet() {
      Wallet wallet = new Wallet();
      wallet.setName(DEFAULT_WALLET_NAME);
      wallet.setBalance(BigDecimal.ZERO);
      return wallet;
   }

   private void addDefaultWallet(User user) {
      Wallet defaultWallet = createDefaultWallet();
      defaultWallet.setOwner(user);
      defaultWallet.setUsers(Collections.singleton(user));
      user.addWallet(defaultWallet);
      walletProvider.save(defaultWallet);
   }
}
