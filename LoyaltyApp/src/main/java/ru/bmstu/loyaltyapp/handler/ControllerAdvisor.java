package ru.bmstu.loyaltyapp.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.bmstu.loyaltyapp.exception.BaseException;
import ru.bmstu.loyaltyapp.exception.data.jwtToken.*;

@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler({
            JwtParsingException.class,
            TokenExpiredException.class,
            JwtEmptyException.class,
            KeyFactoryErrorException.class})
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
