package uz.booker.bookstore.custom.exception;


import jakarta.servlet.ServletException;

public class NotFoundException extends ServletException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
