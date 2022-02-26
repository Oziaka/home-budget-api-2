package pl.wallet.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.exception.AppError;

@Getter
@AllArgsConstructor
public enum CategoryError implements AppError {
    NO_YOUR_PROPERTY("There is no your property", HttpStatus.BAD_REQUEST),
    CAN_NOT_EDIT_DEFAULT("You can not edit no default or not exist category", HttpStatus.BAD_REQUEST);
    private String message;
    private HttpStatus status;
}
