package ru.bmstu.reservationapp.exception.data.jwtToken;

import org.springframework.http.HttpStatus;
import ru.bmstu.reservationapp.exception.BaseException;


public class JwtEmptyException extends BaseException {
    public static String message = "Jwt token is empty";

    public JwtEmptyException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }
}