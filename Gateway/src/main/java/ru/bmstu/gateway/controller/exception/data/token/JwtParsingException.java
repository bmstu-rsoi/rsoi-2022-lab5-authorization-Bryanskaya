package ru.bmstu.gateway.controller.exception.data.token;

import org.springframework.http.HttpStatus;
import ru.bmstu.gateway.controller.exception.BaseException;

public class JwtParsingException extends BaseException {
    public static String message = "Jwt parsing error";

    public JwtParsingException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }

    public JwtParsingException(String msg, HttpStatus codeStatus) {
        super(message + ": " + msg, codeStatus);
    }

}
