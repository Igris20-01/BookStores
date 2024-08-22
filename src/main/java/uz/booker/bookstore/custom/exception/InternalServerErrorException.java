package uz.booker.bookstore.custom.exception;


import jakarta.servlet.ServletException;

public class InternalServerErrorException extends ServletException {
    public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
