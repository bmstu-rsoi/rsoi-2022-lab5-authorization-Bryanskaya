package ru.bmstu.paymentapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.paymentapp.service.PaymentService;

import javax.websocket.server.PathParam;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;


    @GetMapping(value = "/{paymentUid}", produces = "application/json")
    public ResponseEntity<?> getPaymentByUid(@PathVariable UUID paymentUid) {
        log.info(">>> PAYMENT: Request to get payment by paymentUid={} was caught.", paymentUid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paymentService.getPaymentByUid(paymentUid));
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<?> postPayment(@PathParam(value = "price") Integer price) {
        log.info(">>> PAYMENT: Request to post new reservation was caught.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paymentService.postPayment(price));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{paymentUid}", produces = "application/json")
    public void cancelPayment(@PathVariable(value = "paymentUid") UUID paymentUid) {
        log.info(">>> PAYMENT: Request to cancel payment={} was caught.", paymentUid);

        paymentService.cancelPayment(paymentUid);
    }
}
