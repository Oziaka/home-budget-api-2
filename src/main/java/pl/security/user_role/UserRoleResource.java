package pl.security.user_role;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.user.UserDto;
import pl.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/user_role")
@AllArgsConstructor
public class UserRoleResource {

  private UserRoleService userRoleService;
  private UserService userService;

  @GetMapping
  public ResponseEntity<List<UserRoleDto>> getUserRoles() {
    return ResponseEntity.ok(userRoleService.getAllRoles());
  }

  @PostMapping("/admin/{userRoleId}/edit")
  public ResponseEntity<UserRoleDto> editRole(@RequestBody UserRoleDto userRoleDto, @PathVariable Long userRoleId) {
    return ResponseEntity.ok(userRoleService.updateRole(userRoleDto, userRoleId));
  }

  @PostMapping("/admin/{userRoleId}/grant_permission/{email}")
  public ResponseEntity<UserDto> grantPermission(@PathVariable Long userRoleId, @PathVariable String email) {
    return ResponseEntity.ok(userService.grantPermission(userRoleId, email));
  }

  @PostMapping("/admin/{userRoleId}/revoke_permission/{email}")
  public ResponseEntity<UserDto> revokePermission(@PathVariable Long userRoleId, @PathVariable String email) {
    return ResponseEntity.ok(userService.revokePermission(userRoleId, email));
  }

}
