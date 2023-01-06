package ru.bmstu.gateway.controller.exception.data;

public class ReservationByUsernameNotFoundException extends RuntimeException {
    public static String MSG = "GATEWAY: reservation for username=%s was not found.";

    public ReservationByUsernameNotFoundException(String username) {
        super(String.format(MSG, username));
    }
}