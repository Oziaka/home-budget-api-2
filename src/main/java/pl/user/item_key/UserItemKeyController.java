package pl.user.item_key;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class UserItemKeyController {
   private UserItemKeyService userItemKeyService;

   UserItemKeyDto addUserItemKey(UserItemKeyDto userItemKeyDto) {
      UserItemKey userItemKey = UserItemKeyMapper.toEntity(userItemKeyDto);
      UserItemKey savedUserItemKey = userItemKeyService.save(userItemKey);
      return UserItemKeyMapper.toDto(savedUserItemKey);
   }
}
