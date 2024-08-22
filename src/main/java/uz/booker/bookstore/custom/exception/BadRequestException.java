package uz.booker.bookstore.custom.exception;



import jakarta.servlet.ServletException;

import java.io.Serial;

public class BadRequestException extends ServletException {

    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }


}
