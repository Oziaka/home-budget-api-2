package pl.security.user_role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.user.User;
import pl.user.UserDto;
import pl.user.UserMapper;
import pl.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class UserRoleController {

    private UserRoleService userRoleService;
    private UserService userService;

    List<UserRoleDto> getAllRoles() {
        return userRoleService.findAll().stream().map(UserRoleMapper::toDto).collect(Collectors.toList());
    }

    UserRoleDto updateRole(UserRoleDto userRoleDto, Long userRoleId) {
        UserRole role = userRoleService.getRole(userRoleId);
        role.setDescription(userRoleDto.getDescription());
        return UserRoleMapper.toDto(userRoleService.save(role));
    }

    UserDto grantPermission(Long userRoleId, String userEmail) {
        User user = userService.getUser(() -> userEmail);
        UserRole userRole = userRoleService.getRole(userRoleId);
        user.addRole(userRole);
        User save = userService.save(user);
        return UserMapper.toDtoWithRoles(save);
    }

    UserDto revokePermission(Long userRoleId, String userEmail) {
        User user = userService.getUser(() -> userEmail);
        UserRole userRole = userRoleService.getRole(userRoleId);
        user.removeRole(userRole);
        User save = userService.save(user);
        return UserMapper.toDtoWithRoles(save);
    }
}
