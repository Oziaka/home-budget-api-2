package pl.security.user_role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.exception.AppError;

@Getter
@AllArgsConstructor
public enum UserRoleError implements AppError {
    NOT_FOUND("User role not found",HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;
}
