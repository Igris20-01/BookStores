package uz.booker.bookstore.custom.exception;


import jakarta.servlet.ServletException;

public class PaymentRequiredException extends ServletException {

    public PaymentRequiredException() {
        super();
    }

    public PaymentRequiredException(String message) {
        super(message);
    }

    public PaymentRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
