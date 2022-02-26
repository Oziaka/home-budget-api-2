package pl.security.user_role;

import pl.exception.AppError;
import pl.exception.AppRuntimeException;

public class UserRoleException extends AppRuntimeException {
    public UserRoleException(UserRoleError userRoleError) {
        super(userRoleError);
    }
}
