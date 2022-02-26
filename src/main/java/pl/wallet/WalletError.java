package pl.wallet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.exception.AppError;

@Getter
@AllArgsConstructor
public enum WalletError implements AppError {
    NOT_FOUND("Wallet not found", HttpStatus.BAD_REQUEST),
    NO_YOUR_PROPERTY("There is no your property", HttpStatus.BAD_REQUEST);
    private String message;
    private HttpStatus status;
}
