package ru.bmstu.gateway.controller.exception.data;

public class RelatedDataNotFoundException extends RuntimeException {
    public static String MSG = "GATEWAY: additional data (information about hotel or payment) was not found.";

    public RelatedDataNotFoundException() {
        super(MSG);
    }
}
