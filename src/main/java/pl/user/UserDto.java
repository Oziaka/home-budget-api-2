package pl.user;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class UserDto {

   @Null(message = "New user can not have id")
   private Long id;

   @UniqueUserEmail(message = "Email is used by another account")
   @Email(message = "Must be a well-formed email address")
   @NotNull(message = "User must have email")
   private String email;

   @Length(min = 5, message = "Password length must be longer than 5")
   @NotNull(message = "User must have password")
   private String password;

   private Map<String, String> items;

   private String userName;

   @Builder
   public UserDto(@Null(message = "New user can not have id") Long id, @Email(message = "Must be a well-formed email address") @NotNull(message = "User must have email") String email, @Length(min = 5, message = "Password length must be longer than 5") @NotNull(message = "User must have password") String password, Map<String, String> items, String userName) {
      this.id = id;
      this.email = email;
      this.password = password;
      this.items = items;
      this.userName = userName;
   }

   public void addItem(String key, String value) {
      if (items == null)
         items = new HashMap<>();
      items.compute(key, (k, l) -> value);
   }
}
