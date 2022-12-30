package ru.bmstu.gateway.controller.exception.service;


public class GatewayErrorException extends RuntimeException {
    public static String MSG = "GATEWAY: error was caught, err=%s.";

    public GatewayErrorException(String err) {
        super(String.format(MSG, err));
    }
}
