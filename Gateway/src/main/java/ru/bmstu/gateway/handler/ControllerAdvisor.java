package ru.bmstu.gateway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.bmstu.gateway.controller.exception.data.*;
import ru.bmstu.gateway.controller.exception.service.*;

@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(HotelServiceNotAvailableException.class)
    public ResponseEntity<?> handleHotelServiceNotAvailableException(HotelServiceNotAvailableException ex) {
        Error err = new Error()
                .setMessage(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .setDescription(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(err);
    }

    @ExceptionHandler(PaymentServiceNotAvailableException.class)
    public ResponseEntity<?> handlePaymentServiceNotAvailableException(PaymentServiceNotAvailableException ex) {
        Error err = new Error()
                .setMessage(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .setDescription(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(err);
    }

    @ExceptionHandler(ReservationServiceNotAvailableException.class)
    public ResponseEntity<?> handleReservationServiceNotAvailableException(ReservationServiceNotAvailableException ex) {
        Error err = new Error()
                .setMessage(ex.code)
                .setDescription(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.valueOf(ex.code))
                .body(err);
    }

    @ExceptionHandler(LoyaltyServiceNotAvailableException.class)
    public ResponseEntity<?> handleLoyaltyServiceNotAvailableException(LoyaltyServiceNotAvailableException ex) {
        Error err = new Error()
                .setMessage(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .setDescription(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(err);
    }

    @ExceptionHandler(GatewayErrorException.class)
    public ResponseEntity<?> handleGatewayErrorException(GatewayErrorException ex) {
        Error err = new Error()
                .setMessage(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .setDescription(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(err);
    }

    @ExceptionHandler(ReservationByUsernameNotFoundException.class)
    public ResponseEntity<?> handleReservationByUsernameNotFoundException(ReservationByUsernameNotFoundException ex) {
        Error err = new Error()
                .setMessage(HttpStatus.NOT_FOUND.toString())
                .setDescription(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(err);
    }


    @ExceptionHandler(RelatedDataNotFoundException.class)
    public ResponseEntity<?> handleRelatedDataNotFoundException(RelatedDataNotFoundException ex) {
        Error err = new Error()
                .setMessage(HttpStatus.NOT_FOUND.toString())
                .setDescription(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(err);
    }

    @ExceptionHandler(ReservationByUsernameReservationUidNotFoundException.class)
    public ResponseEntity<?> handleReservationByUsernameReservationUidNotFoundException(ReservationByUsernameReservationUidNotFoundException ex) {
        Error err = new Error()
                .setMessage(HttpStatus.NOT_FOUND.toString())
                .setDescription(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(err);
    }


    @ExceptionHandler(RequestDataErrorException.class)
    public ResponseEntity<?> handleRequestDataErrorException(RequestDataErrorException ex) {
        Error err = new Error()
                .setMessage(HttpStatus.BAD_REQUEST.toString())
                .setDescription(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(err);
    }

}
