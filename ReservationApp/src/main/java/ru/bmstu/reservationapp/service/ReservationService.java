package ru.bmstu.reservationapp.service;

import ru.bmstu.reservationapp.dto.ReservationDTO;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    List<ReservationDTO> getReservationsByUsername(String username);
    ReservationDTO getReservationsByUsernameReservationUid(String username, UUID reservationUid);
    ReservationDTO postReservation(String username, ReservationDTO reservationDTO);
    void revokeReservation(String username, UUID reservationUid);
}
