package ru.bmstu.gateway.controller.exception.service;

import org.springframework.http.HttpStatus;
import ru.bmstu.gateway.controller.exception.BaseException;

public class PaymentServiceNotAvailableException extends BaseException {
    public static String message = "Payment Service unavailable";

    public PaymentServiceNotAvailableException(HttpStatus codeStatus) {
        super(message, codeStatus);
    }
}