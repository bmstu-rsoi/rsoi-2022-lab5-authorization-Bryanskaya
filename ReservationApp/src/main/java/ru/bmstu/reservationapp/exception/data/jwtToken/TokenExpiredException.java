package ru.bmstu.reservationapp.exception.data.jwtToken;

import org.springframework.http.HttpStatus;
import ru.bmstu.reservationapp.exception.BaseException;


public class TokenExpiredException extends BaseException {
    public static String message = "Token expired";
    public static HttpStatus codeStatusDefault = HttpStatus.UNAUTHORIZED;

    public TokenExpiredException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }

    public TokenExpiredException() {
        super(message, codeStatusDefault);
    }
}
