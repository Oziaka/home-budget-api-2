package pl.wallet.transaction.exception;

import pl.exception.AppRuntimeException;

public class TransactionException extends AppRuntimeException {
    public TransactionException(TransactionError walletError) {
        super(walletError);
    }
}
