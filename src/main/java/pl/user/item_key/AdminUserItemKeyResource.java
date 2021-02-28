package pl.user.item_key;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/user_item_key")
public class AdminUserItemKeyResource {

   private UserItemKeyService userItemKeyService;

   @PutMapping("/add")
   public ResponseEntity<UserItemKeyDto> addUserItemKey(@RequestBody UserItemKeyDto userItemKeyDto) {
      return ResponseEntity.status(CREATED).body(userItemKeyService.addUserItemKey(userItemKeyDto));
   }
}
