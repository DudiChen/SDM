package exception;

public class OrderValidationException extends Exception {

    public OrderValidationException(String message) {
        super(message);
    }

    public OrderValidationException() {
    }
}
