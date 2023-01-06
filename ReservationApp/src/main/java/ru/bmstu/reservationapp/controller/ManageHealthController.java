package ru.bmstu.reservationapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/manage/health")
public class ManageHealthController {
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> isAlive() {
        return ResponseEntity
                .ok()
                .build();
    }
}