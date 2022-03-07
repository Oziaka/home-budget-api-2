package pl.wallet.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.exception.AppError;

@Getter
@AllArgsConstructor
public enum CurrencyError implements AppError {
    ;
    private final String message;
    private final HttpStatus status;
}
