package ru.bmstu.reservationapp.exception.data.jwtToken;

import org.springframework.http.HttpStatus;
import ru.bmstu.reservationapp.exception.BaseException;


public class UnauthorizedException extends BaseException {
    public static String message = "User unauthorized";
    public static HttpStatus codeStatus = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException() {
        super(message, codeStatus);
    }

    public UnauthorizedException(String msg) {
        super(message + ": " + msg, codeStatus);
    }
}
