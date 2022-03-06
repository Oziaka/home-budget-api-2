package pl.security.user_role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserRoleProvider {

    private final UserRoleRepository userRoleRepository;
    public List<UserRole> getDefaults() {
        return userRoleRepository.getDefaultRoles();
    }

    public UserRole getOne(Long userRoleId) {
        return userRoleRepository.findById(userRoleId).orElseThrow(() -> new UserRoleException(UserRoleError.NOT_FOUND));
    }

}
