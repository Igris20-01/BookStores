package uz.booker.bookstore.dto;

import lombok.Getter;

@Getter
public class ApiErrorResponse {

    private final int code;

    private final String message;

    public ApiErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
