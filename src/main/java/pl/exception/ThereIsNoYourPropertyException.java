package pl.exception;

public class ThereIsNoYourPropertyException extends RuntimeException {
    public ThereIsNoYourPropertyException() {
        super("There is no your property");
    }
}

