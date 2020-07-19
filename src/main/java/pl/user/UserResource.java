package pl.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Validated
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class UserResource {

    private UserController userController;

    @PutMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userController.addUserWithDefaultsResources(userDto));
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @GetMapping(value = "user/profile", consumes = MediaType.ALL_VALUE)
    public UserDto getUser(Principal principal) {
        return userController.getUserByPrincipal(principal);
    }

    @PostMapping(value = "user/edit")
    public UserDto editUser(Principal principal, @RequestBody UserDto userDto) {
        return userController.editUser(principal, userDto);
    }

}
