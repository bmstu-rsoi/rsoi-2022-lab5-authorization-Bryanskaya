package ru.bmstu.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.gateway.dto.TokenRequest;
import ru.bmstu.gateway.service.TokenService;

@Slf4j
@RestController
public class TokenController {
    @Autowired
    private TokenService service;

//    @Autowired
//    private AuthenticationManager


    @PostMapping(value = "/oauth/token", consumes = {"application/x-www-form-urlencoded"})
    public ResponseEntity<?> getToken(TokenRequest data) {
        return ResponseEntity
                .ok(service.generateToken(data));
    }


}
