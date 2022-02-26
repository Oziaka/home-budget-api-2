package pl.wallet.category;

import pl.exception.AppRuntimeException;

public class CategoryException extends AppRuntimeException {
    public CategoryException(CategoryError walletError) {
        super(walletError);
    }
}
