package ru.bmstu.loyaltyapp.service;

import ru.bmstu.loyaltyapp.dto.LoyaltyIntoResponse;


public interface LoyaltyService {
    Integer getDiscountByUsername(String bearerToken);
    LoyaltyIntoResponse getLoyaltyInfoResponseByUsername(String bearerToken);
    Integer getReservationUpdatedPrice(Integer price, Integer discount);
    LoyaltyIntoResponse updateReservationCount(String bearerToken);
    LoyaltyIntoResponse cancelReservationCount(String bearerToken);
}
