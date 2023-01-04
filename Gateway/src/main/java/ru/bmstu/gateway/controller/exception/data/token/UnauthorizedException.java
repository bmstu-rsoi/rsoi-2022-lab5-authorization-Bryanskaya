package ru.bmstu.gateway.controller.exception.data.token;

import org.springframework.http.HttpStatus;
import ru.bmstu.gateway.controller.exception.BaseException;

public class UnauthorizedException extends BaseException {
    public static String message = "User unauthorized";

    public UnauthorizedException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }
}