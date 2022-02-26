package pl.user;

import pl.exception.AppRuntimeException;

public class UserException extends AppRuntimeException {
    public UserException(UserError userError) {
        super(userError);
    }
}
