package ru.bmstu.loyaltyapp.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.loyaltyapp.dto.LoyaltyIntoResponse;
import ru.bmstu.loyaltyapp.service.LoyaltyService;

import javax.websocket.server.PathParam;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping({"api/v1/loyalty", ""})
public class LoyaltyController {
    private final LoyaltyService loyaltyService;

    @GetMapping(value = "/manage/health", produces = "application/json")
    public ResponseEntity<?> isAlive() {
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getDiscountByUsername(@RequestHeader(value = "X-User-Name") String username) {
        log.info(">>> LOYALTY: Request to get username's={} loyalty status was caught.", username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loyaltyService.getDiscountByUsername(username));
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<?> getLoyaltyInfoResponseByUsername(@RequestHeader(value = "X-User-Name") String username) {
        log.info(">>> LOYALTY: Request to get username's={} loyalty info was caught.", username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loyaltyService.getLoyaltyInfoResponseByUsername(username));
    }

    @GetMapping(value = "/update", produces = "application/json")
    public ResponseEntity<Integer> getReservationUpdatedPrice(@PathParam(value = "price") Integer price,
                                                        @PathParam(value = "discount") Integer discount) {
        log.info(">>> LOYALTY: Request to get updated price was caught.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loyaltyService.getReservationUpdatedPrice(price, discount));
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<LoyaltyIntoResponse> updateReservationCount(@RequestHeader(value = "X-User-Name") String username) {
        log.info(">>> LOYALTY: Request to update reservation count got user={} was caught.", username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loyaltyService.updateReservationCount(username));
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<LoyaltyIntoResponse> cancelReservationCount(@RequestHeader(value = "X-User-Name") String username) {
        log.info(">>> LOYALTY: Request to cancel reservation count user={} was caught.", username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loyaltyService.cancelReservationCount(username));
    }
}
