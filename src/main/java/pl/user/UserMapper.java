package pl.user;


public class UserMapper {

    private UserMapper() {
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .items(user.getItems())
                .build();
    }


    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto toDtoWithRoles(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .build();
    }
}
