package pl.user;


import pl.security.user_role.UserRoleMapper;

import java.util.stream.Collectors;

public class UserMapper {

  private UserMapper () {
  }

  public static UserDto toDto (User user) {
    return UserDto.builder()
      .email(user.getEmail())
      .favoriteWalletId(user.getFavoriteWalletId())
      .build();
  }


  public static User toEntity (UserDto userDto) {
    User user = new User();
    user.setPassword(userDto.getPassword());
    user.setEmail(userDto.getEmail());
    return user;
  }

  public static UserDto toDtoWithRoles (User user) {
    return UserDto.builder()
      .email(user.getEmail())
      .roles(user.getRoles().stream().map(UserRoleMapper::toDto).collect(Collectors.toSet()))
      .build();
  }
}
