package pl.wallet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import pl.exception.AppError;

@Getter
@AllArgsConstructor
public enum WalletError implements AppError {
    NOT_FOUND("Wallet not found", HttpStatus.BAD_REQUEST),
    NOT_YOUR_PROPERTY("There is no your property", HttpStatus.BAD_REQUEST),
    ONLY_OWNER_CAN_REMOVE_WALLET("Only owner can remove wallet", HttpStatus.BAD_REQUEST),
    NEW_OWNER_SHOULD_BY_IN_WALLET_USERS("New owner sbould be in wallet users", HttpStatus.BAD_REQUEST),
    CAN_NOT_REMOVE_OWNER_FROM_WALLET("Can not remove owner from wallet", HttpStatus.BAD_REQUEST);
    private String message;
    private HttpStatus status;
}
