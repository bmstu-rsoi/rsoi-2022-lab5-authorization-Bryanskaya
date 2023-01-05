package ru.bmstu.gateway.controller.exception.data.token;

import org.springframework.http.HttpStatus;
import ru.bmstu.gateway.controller.exception.BaseException;

public class KeyFactoryErrorException extends BaseException {
    public static String message = "Key factory error";

    public KeyFactoryErrorException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }

    public KeyFactoryErrorException(String msg, HttpStatus codeStatus) {
        super(message + ": " + msg, codeStatus);
    }
}
