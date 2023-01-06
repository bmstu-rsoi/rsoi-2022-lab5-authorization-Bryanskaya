package ru.bmstu.reservationapp.exception.data.jwtToken;

import org.springframework.http.HttpStatus;
import ru.bmstu.reservationapp.exception.BaseException;

public class KeyFactoryErrorException extends BaseException {
    public static String message = "Key factory error";

    public KeyFactoryErrorException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }

    public KeyFactoryErrorException(String msg, HttpStatus codeStatus) {
        super(message + ": " + msg, codeStatus);
    }
}
