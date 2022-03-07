package pl.wallet.currency;

import pl.exception.AppRuntimeException;

public class CurrencyException extends AppRuntimeException {
    public CurrencyException(CurrencyError currencyError) {
        super(currencyError);
    }
}

