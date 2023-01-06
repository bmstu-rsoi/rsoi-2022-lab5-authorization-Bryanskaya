package ru.bmstu.loyaltyapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.loyaltyapp.dto.LoyaltyIntoResponse;
import ru.bmstu.loyaltyapp.service.LoyaltyService;
import ru.bmstu.loyaltyapp.service.TokenService;

import javax.websocket.server.PathParam;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/loyalty")
public class LoyaltyController {
    private final LoyaltyService loyaltyService;

    @Autowired
    private final TokenService tokenService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getDiscountByUsername(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        log.info(">>> LOYALTY: Request to get username's loyalty status was caught.");

        tokenService.validateToken(bearerToken);

        return ResponseEntity
                .ok()
                .body(loyaltyService.getDiscountByUsername(bearerToken));
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<?> getLoyaltyInfoResponseByUsername(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        log.info(">>> LOYALTY: Request to get username's loyalty info was caught.");

        tokenService.validateToken(bearerToken);

        LoyaltyIntoResponse info = loyaltyService.getLoyaltyInfoResponseByUsername(bearerToken);

        return ResponseEntity
                .ok()
                .body(info);
    }

    @GetMapping(value = "/update", produces = "application/json")
    public ResponseEntity<Integer> getReservationUpdatedPrice(@PathParam(value = "price") Integer price,
                                                        @PathParam(value = "discount") Integer discount) {
        log.info(">>> LOYALTY: Request to get updated price was caught.");

        return ResponseEntity
                .ok()
                .body(loyaltyService.getReservationUpdatedPrice(price, discount));
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<LoyaltyIntoResponse> updateReservationCount(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        log.info(">>> LOYALTY: Request to update reservation count got user was caught.");

        tokenService.validateToken(bearerToken);

        return ResponseEntity
                .ok()
                .body(loyaltyService.updateReservationCount(bearerToken));
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<LoyaltyIntoResponse> cancelReservationCount(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
        log.info(">>> LOYALTY: Request to cancel reservation count user was caught.");

        tokenService.validateToken(bearerToken);

        return ResponseEntity
                .ok()
                .body(loyaltyService.cancelReservationCount(bearerToken));
    }
}
