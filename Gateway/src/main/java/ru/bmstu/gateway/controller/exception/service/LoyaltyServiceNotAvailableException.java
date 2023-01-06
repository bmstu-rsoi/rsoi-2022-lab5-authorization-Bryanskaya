package ru.bmstu.gateway.controller.exception.service;

import org.springframework.http.HttpStatus;
import ru.bmstu.gateway.controller.exception.BaseException;

public class LoyaltyServiceNotAvailableException extends BaseException {
    public static String message = "Loyalty Service unavailable";

    public LoyaltyServiceNotAvailableException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }
}