package pl.security.user_role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.UserRoleOfSuchIdDoNotExistException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRoleService {

    private UserRoleRepository userRoleRepository;

    public List<UserRole> findDefaultRoles() {
        return userRoleRepository.getDefaultRoles();
    }


    List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    UserRole getRole(Long userRoleId) {
        return userRoleRepository.findById(userRoleId).orElseThrow(UserRoleOfSuchIdDoNotExistException::new);
    }

    UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }
}
