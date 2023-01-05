package ru.bmstu.gateway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.bmstu.gateway.controller.exception.BaseException;
import ru.bmstu.gateway.controller.exception.data.*;
import ru.bmstu.gateway.controller.exception.data.token.*;
import ru.bmstu.gateway.controller.exception.service.*;

@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler({
            HotelServiceNotAvailableException.class,
            LoyaltyServiceNotAvailableException.class,
            PaymentServiceNotAvailableException.class,
            ReservationServiceNotAvailableException.class,
            UnauthorizedException.class,
            TokenExpiredException.class,
            JwtParsingException.class,
            JwtEmptyException.class,
            KeyFactoryErrorException.class})
    public ResponseEntity<?> handleHotelServiceNotAvailableException(BaseException ex) {
        Error err = new Error()
                .setCode(ex.code)
                .setMessage(ex.getMessage());

        return ResponseEntity
                .status(ex.code)
                .body(err);
    }

    @ExceptionHandler(GatewayErrorException.class)
    public ResponseEntity<?> handleGatewayErrorException(GatewayErrorException ex) {
        Error err = new Error()
                .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(err);
    }

    @ExceptionHandler({
            ReservationByUsernameNotFoundException.class,
            RelatedDataNotFoundException.class,
            ReservationByUsernameReservationUidNotFoundException.class
    })
    public ResponseEntity<?> handleReservationByUsernameNotFoundException(ReservationByUsernameNotFoundException ex) {
        Error err = new Error()
                .setCode(HttpStatus.NOT_FOUND.value())
                .setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(err);
    }

    @ExceptionHandler(RequestDataErrorException.class)
    public ResponseEntity<?> handleRequestDataErrorException(RequestDataErrorException ex) {
        Error err = new Error()
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(err);
    }
}
