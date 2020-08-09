package pl.security.user_role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.exception.EntityNotFoundException;
import pl.exception.UserRoleOfSuchIdDoNotExistException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRoleService {

   private UserRoleRepository userRoleRepository;

   public List<UserRole> getDefaults() {
      return userRoleRepository.getDefaultRoles();
   }

   List<UserRole> getAll() {
      return userRoleRepository.findAll();
   }

   UserRole getOne(Long userRoleId) {
      return userRoleRepository.findById(userRoleId).orElseThrow(() -> new EntityNotFoundException(userRoleId, UserRole.class));
   }

   UserRole save(UserRole userRole) {
      return userRoleRepository.save(userRole);
   }
}
