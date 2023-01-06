package ru.bmstu.reservationapp.service;


public interface TokenService {
    public boolean validateToken(String token);
}
