package pl.user.item_key;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserItemKeyService {
   private UserItemKeyRepository userItemKeyRepository;


   public UserItemKey save(UserItemKey userItemKey) {
      return userItemKeyRepository.save(userItemKey);
   }

   public List<UserItemKey> getAll() {
      return userItemKeyRepository.findAll();
   }
}
