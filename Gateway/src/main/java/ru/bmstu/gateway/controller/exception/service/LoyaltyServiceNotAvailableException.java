package ru.bmstu.gateway.controller.exception.service;

public class LoyaltyServiceNotAvailableException extends RuntimeException {
    public static String MSG = "GATEWAY: Loyalty service is not available, code=%s.";

    public LoyaltyServiceNotAvailableException(String codeStatus) {
        super(String.format(MSG, codeStatus));
    }

}
