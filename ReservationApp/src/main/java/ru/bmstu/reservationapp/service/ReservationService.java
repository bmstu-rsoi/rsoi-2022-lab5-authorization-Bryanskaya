package ru.bmstu.reservationapp.service;

import ru.bmstu.reservationapp.dto.ReservationDTO;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    List<ReservationDTO> getReservationsByUsername(String bearerToken);
    ReservationDTO getReservationsByUsernameReservationUid(String bearerToken, UUID reservationUid);
    ReservationDTO postReservation(String bearerToken, ReservationDTO reservationDTO);
    void revokeReservation(String bearerToken, UUID reservationUid);
}
