package pl.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@AllArgsConstructor
public class UserUniqueValidator implements ConstraintValidator<UniqueUserEmail, String> {

   private UserService userService;

   @Override
   public void initialize(UniqueUserEmail uniqueUserEmail) {

   }

   @Override
   public boolean isValid(String email, ConstraintValidatorContext context) {
      return !userService.emailIsUsed(email);
   }
}
