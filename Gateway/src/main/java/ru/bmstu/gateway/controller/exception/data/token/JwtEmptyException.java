package ru.bmstu.gateway.controller.exception.data.token;

import org.springframework.http.HttpStatus;
import ru.bmstu.gateway.controller.exception.BaseException;

public class JwtEmptyException extends BaseException {
    public static String message = "Jwt token is empty";

    public JwtEmptyException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }
}
