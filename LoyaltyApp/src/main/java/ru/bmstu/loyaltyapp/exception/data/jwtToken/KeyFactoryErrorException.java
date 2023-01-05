package ru.bmstu.loyaltyapp.exception.data.jwtToken;

import org.springframework.http.HttpStatus;
import ru.bmstu.loyaltyapp.exception.BaseException;


public class KeyFactoryErrorException extends BaseException {
    public static String message = "Key factory error";

    public KeyFactoryErrorException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }

    public KeyFactoryErrorException(String msg, HttpStatus codeStatus) {
        super(message + ": " + msg, codeStatus);
    }
}
