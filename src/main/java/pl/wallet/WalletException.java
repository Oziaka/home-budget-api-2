package pl.wallet;

import pl.exception.AppError;
import pl.exception.AppRuntimeException;

public class WalletException extends AppRuntimeException {
    public WalletException(WalletError walletError) {
        super(walletError);
    }
}
