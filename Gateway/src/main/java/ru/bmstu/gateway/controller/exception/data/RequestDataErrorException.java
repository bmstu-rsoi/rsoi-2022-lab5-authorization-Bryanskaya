package ru.bmstu.gateway.controller.exception.data;

public class RequestDataErrorException extends RuntimeException {
    public static String MSG = "GATEWAY: request body is not correct (data=%s).";

    public  RequestDataErrorException(String data) {
        super(String.format(MSG, data));
    }
}