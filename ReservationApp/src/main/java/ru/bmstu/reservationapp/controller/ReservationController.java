package ru.bmstu.reservationapp.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.reservationapp.dto.ReservationDTO;
import ru.bmstu.reservationapp.service.ReservationService;
import ru.bmstu.reservationapp.service.TokenService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reservations")
public class ReservationController {
    @Autowired
    private final TokenService tokenService;

    private final ReservationService reservationService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getReservationsByUsername(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        log.info(">>> RESERVATION: Request to get all reservations by username was caught.");

        tokenService.validateToken(bearerToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reservationService.getReservationsByUsername(bearerToken));
    }

    @GetMapping(value = "/{reservationUid}", produces = "application/json")
    public ResponseEntity<?> getReservationsByUsernameReservationUid(@RequestHeader(value = "Authorization", required = false) String bearerToken,
                                                                     @PathVariable(value = "reservationUid") UUID reservationUid) {
        log.info(">>> RESERVATION: Request to get all reservations by username and reservationUid was caught.");

        tokenService.validateToken(bearerToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reservationService.getReservationsByUsernameReservationUid(bearerToken, reservationUid));
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<?> postReservation(@RequestHeader(value = "Authorization", required = false) String bearerToken,
                                             @RequestBody ReservationDTO reservationDTO) {
        log.info(">>> RESERVATION: Request to post a new reservation for user was caught.");

        tokenService.validateToken(bearerToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reservationService.postReservation(bearerToken, reservationDTO));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{reservationUid}")
    public void revokeReservation(@RequestHeader(value = "Authorization", required = false) String bearerToken,
                                  @PathVariable(value = "reservationUid") UUID reservationUid) {
        log.info(">>> RESERVATION: Request to revoke reservation was caught (reservationUid={}).", reservationUid);

        tokenService.validateToken(bearerToken);

        reservationService.revokeReservation(bearerToken, reservationUid);
    }
}
