package pl.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.exception.AppError;

@Getter
@AllArgsConstructor
public enum UserError implements AppError {
    NOT_FOUND("User not found",HttpStatus.NOT_FOUND),
    NOT_UNIQUE_EMAIL("User must have unique email",HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
