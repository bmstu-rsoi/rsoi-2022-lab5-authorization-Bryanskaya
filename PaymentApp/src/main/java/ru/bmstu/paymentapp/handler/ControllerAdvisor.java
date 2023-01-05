package ru.bmstu.paymentapp.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.bmstu.paymentapp.exception.BaseException;
import ru.bmstu.paymentapp.exception.data.jwtToken.JwtEmptyException;
import ru.bmstu.paymentapp.exception.data.jwtToken.JwtParsingException;
import ru.bmstu.paymentapp.exception.data.jwtToken.TokenExpiredException;
import ru.bmstu.paymentapp.exception.data.jwtToken.UnauthorizedException;


@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler({
            JwtParsingException.class,
            TokenExpiredException.class,
            JwtEmptyException.class})
    public ResponseEntity<?> handleWrongDataException(BaseException ex) {
        Error err = new Error()
                .setCode(ex.code)
                .setMessage(ex.getMessage());

        return ResponseEntity
                .status(ex.code)
                .body(err);
    }

    @ExceptionHandler({
            UnauthorizedException.class
    })
    public ResponseEntity<?> handleUnauthorizedUserException(BaseException ex) {
        Error err = new Error()
                .setCode(ex.code)
                .setMessage(ex.getMessage());

        return ResponseEntity
                .status(ex.code)
                .body(err);
    }
}
