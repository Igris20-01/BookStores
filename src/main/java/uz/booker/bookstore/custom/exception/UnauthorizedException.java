package uz.booker.bookstore.custom.exception;


import jakarta.servlet.ServletException;

public class UnauthorizedException extends ServletException {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
