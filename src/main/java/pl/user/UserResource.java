package pl.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@AllArgsConstructor
@CrossOrigin("${cors.allowed-origins}")
public class UserResource {

   private UserService userService;

   @PutMapping(path = "/register", consumes = APPLICATION_JSON_VALUE,produces = APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> register(@RequestBody @Valid UserDto userDto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUserWithDefaultsResources(userDto));
   }

   @RequestMapping("/user")
   public Principal user(Principal principal) {
      return principal;
   }

   @GetMapping(path = "/user/profile", consumes = MediaType.ALL_VALUE)
   public ResponseEntity<UserDto> getUser(Principal principal) {
      return ResponseEntity.ok(userService.getProfile(principal));
   }

   @PostMapping(path = "/user/edit", consumes = APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> editUser(Principal principal, @RequestBody UserDto userDto) {
      return ResponseEntity.ok(userService.editUser(principal, userDto));
   }


}
