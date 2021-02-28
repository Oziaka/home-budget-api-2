package pl.user;


public class UserMapper {

   private UserMapper() {
   }

   public static UserDto toDtoForSelf(User user) {
      return UserDto.builder()
         .email(user.getEmail())
         .userName(user.getUserName())
         .items(user.getItems())
         .build();
   }


   public static User toEntity(UserDto userDto) {
      return User.builder()
         .email(userDto.getEmail())
         .password(userDto.getPassword())
         .userName(userDto.getUserName())
         .build();
   }

   public static UserDto toDto(User user) {
      return UserDto.builder()
         .email(user.getEmail())
         .userName(user.getUserName())
         .build();

   }


   public static UserDto toDtoWithRoles(User user) {
      return UserDto.builder()
         .email(user.getEmail())
         .userName(user.getUserName())
         .build();
   }
}
