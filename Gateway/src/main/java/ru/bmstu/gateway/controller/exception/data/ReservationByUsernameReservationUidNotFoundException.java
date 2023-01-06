package ru.bmstu.gateway.controller.exception.data;

import java.util.UUID;

public class ReservationByUsernameReservationUidNotFoundException extends RuntimeException {
    public static String MSG = "GATEWAY: reservation for username=%s reservationUid=%s was not found.";

    public ReservationByUsernameReservationUidNotFoundException(String username, UUID reservationUid) {
        super(String.format(MSG, username, reservationUid));
    }
}