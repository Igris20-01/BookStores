package uz.booker.bookstore.custom.exception;


import jakarta.servlet.ServletException;
import org.springframework.boot.web.servlet.error.ErrorController;

public class MethodNotAllowed extends ServletException implements ErrorController {

    public MethodNotAllowed() {
        super();
    }

    public MethodNotAllowed(String message) {
        super(message);
    }

    public MethodNotAllowed(String message, Throwable cause) {
        super(message, cause);
    }
}
