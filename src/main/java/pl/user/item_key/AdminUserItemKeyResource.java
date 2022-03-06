package pl.user.item_key;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/user_item_key")
public class AdminUserItemKeyResource {

    private final UserItemKeyService userItemKeyService;

    @PostMapping("/add")
    public ResponseEntity<UserItemKeyDto> addUserItemKey(@RequestBody UserItemKeyDto userItemKeyDto) {
        return ResponseEntity.status(CREATED).body(userItemKeyService.addUserItemKey(userItemKeyDto));
    }
}
