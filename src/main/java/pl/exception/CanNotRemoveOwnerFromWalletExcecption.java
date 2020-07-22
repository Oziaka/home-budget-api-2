package pl.exception;

public class CanNotRemoveOwnerFromWalletExcecption extends RuntimeException {
    public CanNotRemoveOwnerFromWalletExcecption() {
        super("Can not remove yourself from wallet because you are owner");
    }
}
