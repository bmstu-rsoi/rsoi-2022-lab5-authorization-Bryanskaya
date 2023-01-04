package ru.bmstu.loyaltyapp.exception.data.jwtToken;

import org.springframework.http.HttpStatus;
import ru.bmstu.loyaltyapp.exception.BaseException;


public class JwtEmptyException extends BaseException {
    public static String message = "Jwt token is empty";

    public JwtEmptyException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }
}