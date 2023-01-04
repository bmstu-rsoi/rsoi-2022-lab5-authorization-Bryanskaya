package ru.bmstu.gateway.controller.exception.data.token;

import org.springframework.http.HttpStatus;
import ru.bmstu.gateway.controller.exception.BaseException;

public class TokenExpiredException extends BaseException {
    public static String message = "Token expired";

    public TokenExpiredException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }
}
