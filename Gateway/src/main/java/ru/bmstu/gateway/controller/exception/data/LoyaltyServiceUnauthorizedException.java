package ru.bmstu.gateway.controller.exception.data;

import org.springframework.http.HttpStatus;
import ru.bmstu.gateway.handler.BaseException;

public class LoyaltyServiceUnauthorizedException extends BaseException {
    public static String message = "User unauthorized";

    public LoyaltyServiceUnauthorizedException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }
}