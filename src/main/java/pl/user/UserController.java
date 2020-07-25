package pl.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import pl.exception.UserMustHaveUniqueEmailException;
import pl.security.user_role.UserRoleService;
import pl.user.item_key.UserItemKey;
import pl.user.item_key.UserItemKeyService;
import pl.user.user_notification.notification.notification.NotificationService;
import pl.wallet.WalletService;
import pl.wallet.category.CategoryService;

import java.security.Principal;
import java.util.List;

@Controller
//@AllArgsConstructor
public class UserController {

    private UserService userService;
    private UserRoleService userRoleService;
    private PasswordEncoder passwordEncoder;
    private WalletService walletService;
    private CategoryService categoryService;
    private UserItemKeyService userItemKeyService;
    private NotificationService notificationService;

    public UserController(UserService userService, UserRoleService userRoleService, PasswordEncoder passwordEncoder, WalletService walletService, CategoryService categoryService, UserItemKeyService userItemKeyService, NotificationService notificationService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
        this.categoryService = categoryService;
        this.userItemKeyService = userItemKeyService;
        this.notificationService = notificationService;
    }

    UserDto addUserWithDefaultsResources(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        encodePassword(user);
        addDefaultRoles(user);
        User savedUser = userService.save(user);
        addDefaultCategories(user);
        walletService.saveDefaultWallet(user);
        return UserMapper.toDto(savedUser);
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

    UserDto getUser(Principal principal) {
        return UserMapper.toDto(userService.get(principal));
    }

    UserDto editUser(Principal principal, UserDto userDto) {
        if (isUserHasUniqueEmail(principal.getName()))
            throw new UserMustHaveUniqueEmailException();
        User user = userService.get(principal);
        User updatedUser = updateUserFromNotNullFieldsInUserDto(user, userDto);
        User savedUser = userService.save(updatedUser);
        return UserMapper.toDto(savedUser);
    }

    private User updateUserFromNotNullFieldsInUserDto(User user, UserDto userDto) {
        User userToUpdate = user;
        if (userDto.getEmail() != null)
            userToUpdate.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null)
            userToUpdate.setPassword(userDto.getPassword());
        if (userDto.getItems() != null) {
            List<UserItemKey> userItemKeys = userItemKeyService.getAll();
            userDto.getItems()
                    .entrySet().stream().filter(item -> userItemKeys.stream()
                    .map(UserItemKey::getName).anyMatch(itemKey -> itemKey.equals(item.getKey())))
                    .forEach(item -> userToUpdate.addItem(item.getKey(), item.getValue()));
        }
        return userToUpdate;
    }

    private Boolean isUserHasUniqueEmail(String email) {
        return !userService.emailIsUsed(email);
    }

}


