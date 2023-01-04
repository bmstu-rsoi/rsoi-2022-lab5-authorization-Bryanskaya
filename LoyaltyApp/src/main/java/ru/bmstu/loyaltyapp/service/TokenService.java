package ru.bmstu.loyaltyapp.service;


public interface TokenService {
    public boolean validateToken(String token);
}
