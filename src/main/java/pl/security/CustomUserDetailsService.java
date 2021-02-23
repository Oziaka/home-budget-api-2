package pl.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.security.user_role.UserRole;
import pl.user.User;
import pl.user.UserService;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String email) {
    User user = userService.getUser(() -> email);
    return new org.springframework.security.core.userdetails.User(
      user.getEmail(),
      user.getPassword(),
      convertAuthorities(user.getRoles()));
  }

  private Set<GrantedAuthority> convertAuthorities(Collection<UserRole> userRoles) {
    return userRoles.stream().map(userRole -> new SimpleGrantedAuthority(userRole.getRoleName())).collect(Collectors.toSet());
  }
}
