package pl.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.EntityNotFoundException;

import java.security.Principal;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    User get(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(email, email.getClass()));
    }

    public User get(Principal principal) {
        return get(principal.getName());
    }

    boolean emailIsUsed(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}
