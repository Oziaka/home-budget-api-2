package pl.user.item_key;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user_item_key/admin")
public class UserItemKeyResource {

    private UserItemKeyController userItemKeyController;

    @PutMapping("/add")
    public ResponseEntity<UserItemKeyDto> addUserItemKey(@RequestBody UserItemKeyDto userItemKeyDto) {
        return ResponseEntity.ok(userItemKeyController.addUserItemKey(userItemKeyDto));
    }
}
