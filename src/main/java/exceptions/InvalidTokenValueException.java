package exceptions;

public class InvalidTokenValueException extends Exception {
    public InvalidTokenValueException() {
        super("Invalid token value. Should be at least 0");
    }
}
