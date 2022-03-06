package pl.security.user_role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    List<UserRoleDto> getAllRoles() {
        return this.getAll().stream().map(UserRoleMapper::toDto).collect(Collectors.toList());
    }

    UserRoleDto updateRole(UserRoleDto userRoleDto, Long userRoleId) {
        UserRole role = this.getOne(userRoleId);
        role.setDescription(userRoleDto.getDescription());
        return UserRoleMapper.toDto(this.save(role));
    }

    public List<UserRole> getDefaults() {
        return userRoleRepository.getDefaultRoles();
    }

    private List<UserRole> getAll() {
        return userRoleRepository.findAll();
    }

    public UserRole getOne(Long userRoleId) {
        return userRoleRepository.findById(userRoleId).orElseThrow(() -> new UserRoleException(UserRoleError.NOT_FOUND));
    }

    private UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }
}
