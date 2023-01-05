package ru.bmstu.gateway.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.gateway.controller.exception.data.RequestDataErrorException;
import ru.bmstu.gateway.dto.*;
import ru.bmstu.gateway.service.GatewayService;
import ru.bmstu.gateway.service.TokenService;

import javax.websocket.server.PathParam;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1")
public class GatewayController {
    @Autowired
    private GatewayService gatewayService;
    @Autowired
    private TokenService tokenService;


//    @GetMapping(value = "/callback")
//    public ResponseEntity<?> callback(Principal principal){
//        return ResponseEntity
//                .ok("Hello, world!");
//    }

    @GetMapping(value = "/hotels", produces = "application/json")
    public ResponseEntity<?> getHotels(@RequestHeader(value = "Authorization", required = false) String bearerToken,
                                       @PathParam(value = "page") Integer page,
                                       @PathParam(value = "size") Integer size) {
        log.info(">>> GATEWAY: Request to get all hotels was caught (params: page={}, size={}).", page, size);

        tokenService.validateToken(bearerToken);
        return gatewayService.getHotels(page, size);
    }

    @GetMapping(value = "/me", produces = "application/json")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        log.info(">>> GATEWAY: Request to get all reservations by current username was caught.");

        tokenService.validateToken(bearerToken);
        return ResponseEntity
                .ok()
                .body(gatewayService.getUserInfo(bearerToken));
    }

    @GetMapping(value = "/reservations", produces = "application/json")
    public ResponseEntity<?> getReservationsByUsername(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        log.info(">>> GATEWAY: Request to get all reservations by username was caught.");

        tokenService.validateToken(bearerToken);
        return ResponseEntity
                .ok()
                .body(gatewayService.getReservationsList(bearerToken));
    }

    @GetMapping(value = "/reservations/{reservationUid}", produces = "application/json")
    public ResponseEntity<?> getReservationByUsernameReservationUid(@RequestHeader(value = "Authorization", required = false) String bearerToken,
                                                                    @PathVariable(value = "reservationUid") UUID reservationUid) {
        log.info(">>> GATEWAY: Request to get all reservations by username and reservationUid={} was caught.", reservationUid);

        tokenService.validateToken(bearerToken);
        return ResponseEntity
                .ok()
                .body(gatewayService.getReservationByUsernameReservationUid(bearerToken, reservationUid));
    }

    @PostMapping(value = "/reservations")
    public ResponseEntity<?> postReservation(@RequestHeader(value = "Authorization", required = false) String bearerToken,
                                             @RequestBody CreateReservationRequest request) {
        log.info(">>> GATEWAY: Request to create reservation was caught (data={}).", request.toString());

        tokenService.validateToken(bearerToken);

        if (!request.isValid())
            throw new RequestDataErrorException(request.toString());

        return ResponseEntity
                .ok()
                .body(gatewayService.postReservation(bearerToken, request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/reservations/{reservationUid}", produces = "application/json")
    public void cancelReservation(@RequestHeader(value = "Authorization", required = false) String bearerToken,
                                  @PathVariable(value = "reservationUid") UUID reservationUid) {
        log.info(">>> GATEWAY: Request to delete reservation was caught (reservationUid={}).", reservationUid);

        tokenService.validateToken(bearerToken);

        gatewayService.cancelReservation(bearerToken, reservationUid);
    }

    @GetMapping(value = "/loyalty", produces = "application/json")
    public ResponseEntity<?> getLoyaltyInfoResponseByUsername(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        tokenService.validateToken(bearerToken);

        return ResponseEntity
                .ok()
                .body(gatewayService.getLoyaltyInfoResponseByUsername(bearerToken));
    }
}
