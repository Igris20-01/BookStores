package uz.booker.bookstore.custom.exception;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import uz.booker.bookstore.dto.ApiErrorResponse;
import uz.booker.bookstore.util.TelegramBotSender;


@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    TelegramBotSender telegramBotSender;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleBadRequestException(HttpServletRequest request, HttpMessageNotReadableException ex) {
        telegramBotSender.sendErrorToTelegram(request, HttpStatus.BAD_REQUEST.value(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUnauthorizedException(HttpServletRequest request, RuntimeException ex) {
        telegramBotSender.sendErrorToTelegram(request, HttpStatus.UNAUTHORIZED.value(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SpelEvaluationException.class)
    public ResponseEntity<Object> handleForbiddenException(HttpServletRequest request, SpelEvaluationException ex) {
        telegramBotSender.sendErrorToTelegram(request, HttpStatus.FORBIDDEN.value(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(HttpServletRequest request, NoHandlerFoundException ex) {
        telegramBotSender.sendErrorToTelegram(request, HttpStatus.NOT_FOUND.value(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleInternalServerError(HttpServletRequest request, Exception ex) {
        telegramBotSender.sendErrorToTelegram(request, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotAllowedException(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        telegramBotSender.sendErrorToTelegram(request, HttpStatus.METHOD_NOT_ALLOWED.value(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }


}



