package pl.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.DataNotFoundException;

import java.security.Principal;

@Service
@AllArgsConstructor
public class UserProvider {

  private UserRepository userRepository;

  public User get(Principal principal) {
    return userRepository.findByEmail(principal.getName()).orElseThrow(() -> new DataNotFoundException("User not found"));
  }

  public User save(User user) {
    return userRepository.save(user);
  }
}
