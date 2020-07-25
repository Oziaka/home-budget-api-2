package pl.security.user_role;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.user.UserDto;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user_role")
@AllArgsConstructor
public class UserRoleResource {

    private UserRoleController userRoleController;

    @GetMapping
    public ResponseEntity<List<UserRoleDto>> getUserRoles() {
        return ResponseEntity.ok(userRoleController.getAllRoles());
    }

    @PostMapping("/admin/{userRoleId}/edit")
    public ResponseEntity<UserRoleDto> editRole(@RequestBody UserRoleDto userRoleDto, @PathVariable Long userRoleId) {
        return ResponseEntity.ok(userRoleController.updateRole(userRoleDto, userRoleId));
    }

    @PostMapping("/admin/{userRoleId}/grant_permission/{email}")
    public ResponseEntity<UserDto> grantPermission(@PathVariable Long userRoleId, @PathVariable String email) {
        return ResponseEntity.ok(userRoleController.grantPermission(userRoleId, email));
    }

    @PostMapping("/admin/{userRoleId}/revoke_permission/{email}")
    public ResponseEntity<UserDto> revokePermission(@PathVariable Long userRoleId, @PathVariable String email) {
        return ResponseEntity.ok(userRoleController.revokePermission(userRoleId, email));
    }

}
