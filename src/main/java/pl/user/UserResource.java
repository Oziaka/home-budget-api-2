package pl.user;

import io.swagger.models.Response;
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

   private UserController userController;

   @PutMapping(path = "/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> register(@RequestBody @Valid UserDto userDto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(userController.addUserWithDefaultsResources(userDto));
   }

   @RequestMapping("/user")
   public Principal user(Principal principal) {
      return principal;
   }

   @GetMapping(path = "/user/profile", consumes = MediaType.ALL_VALUE)
   public ResponseEntity<UserDto> getUser(Principal principal) {
      return ResponseEntity.ok(userController.getProfile(principal));
   }

   @PostMapping(path = "/user/edit", consumes = APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> editUser(Principal principal, @RequestBody UserDto userDto) {
      return ResponseEntity.ok(userController.editUser(principal, userDto));
   }


}
