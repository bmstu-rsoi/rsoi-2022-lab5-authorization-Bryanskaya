package ru.bmstu.gateway.controller.exception.service;

public class PaymentServiceNotAvailableException extends RuntimeException {
    public static String MSG = "GATEWAY: Payment service is not available, code=%s.";

    public PaymentServiceNotAvailableException(String codeStatus) {
        super(String.format(MSG, codeStatus));
    }
}