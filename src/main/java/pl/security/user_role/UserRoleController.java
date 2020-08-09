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
      return userRoleService.getAll().stream().map(UserRoleMapper::toDto).collect(Collectors.toList());
   }

   UserRoleDto updateRole(UserRoleDto userRoleDto, Long userRoleId) {
      UserRole role = userRoleService.getOne(userRoleId);
      role.setDescription(userRoleDto.getDescription());
      return UserRoleMapper.toDto(userRoleService.save(role));
   }

   UserDto grantPermission(Long userRoleId, String email) {
      User user = userService.get(() -> email);
      UserRole userRole = userRoleService.getOne(userRoleId);
      user.addRole(userRole);
      User save = userService.save(user);
      return UserMapper.toDtoWithRoles(save);
   }

   UserDto revokePermission(Long userRoleId, String email) {
      User user = userService.get(() -> email);
      UserRole userRole = userRoleService.getOne(userRoleId);
      user.removeRole(userRole);
      User save = userService.save(user);
      return UserMapper.toDtoWithRoles(save);
   }
}
