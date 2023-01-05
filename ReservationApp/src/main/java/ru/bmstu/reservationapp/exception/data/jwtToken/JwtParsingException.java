package ru.bmstu.reservationapp.exception.data.jwtToken;

import org.springframework.http.HttpStatus;
import ru.bmstu.reservationapp.exception.BaseException;

public class JwtParsingException extends BaseException {
    public static String message = "Jwt parsing error";

    public JwtParsingException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }

    public JwtParsingException(String msg, HttpStatus codeStatus) {
        super(message + ": " + msg, codeStatus);
    }
}
