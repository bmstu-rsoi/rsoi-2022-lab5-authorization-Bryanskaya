package ru.bmstu.loyaltyapp.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    public static String message = "Exception was caught";
    public int code;

    public BaseException(String msg, HttpStatus codeStatus) {
        super(msg);
        message = msg;
        code = codeStatus.value();
    }
}