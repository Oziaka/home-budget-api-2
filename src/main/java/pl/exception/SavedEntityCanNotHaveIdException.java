package pl.exception;


public class SavedEntityCanNotHaveIdException extends RuntimeException {
    public SavedEntityCanNotHaveIdException() {
        super("New entity can not have id");
    }
}
