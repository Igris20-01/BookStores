package uz.booker.bookstore.custom.exception;


import jakarta.servlet.ServletException;

public class ForbiddenException extends ServletException {

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
