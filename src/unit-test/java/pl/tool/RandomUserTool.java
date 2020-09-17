package pl.tool;

import org.h2.engine.UserBuilder;
import pl.user.User;
import pl.user.UserDto;

import static pl.tool.RandomUtils.randomString;

public class RandomUserTool {

   public static UserDto randomUserDto() {
      return randomUserDtoBuilder().build();
   }
   public static User randomUser() {
      return randomUserBuilder().build();
   }

   public static UserDto.UserDtoBuilder randomUserDtoBuilder() {
      return UserDto.builder()
         .email(randomEmaiL())
         .password(randomString());
   }
   public static User.UserBuilder randomUserBuilder() {
      return User.builder()
         .email(randomEmaiL())
         .password(randomString());
   }

   private static String randomEmaiL() {
      return randomString() + randomString(3) + '@' + randomString(2) + "." + randomString(2);
   }
}
