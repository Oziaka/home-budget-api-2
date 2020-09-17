package pl.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.DataNotFoundExeption;

import java.security.Principal;

@Service
@AllArgsConstructor
public class UserProvider {

   private UserRepository userRepository;

   public User get(Principal principal) {
      return get(principal.getName());
   }

   User get(String email) {
      return userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundExeption("User not found"));
   }

   public User save(User user) {
      return userRepository.save(user);
   }
}
