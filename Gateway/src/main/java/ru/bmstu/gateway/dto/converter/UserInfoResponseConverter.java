package ru.bmstu.gateway.dto.converter;

import ru.bmstu.gateway.dto.LoyaltyInfoResponse;
import ru.bmstu.gateway.dto.ReservationResponse;
import ru.bmstu.gateway.dto.UserInfoResponse;

import java.util.ArrayList;

public class UserInfoResponseConverter {
    public static UserInfoResponse createUserInfoResponse(ArrayList<ReservationResponse> reservationResponses,
                                                          LoyaltyInfoResponse loyaltyInfoResponse) {
        return new UserInfoResponse()
                .setReservations(reservationResponses)
                .setLoyalty(loyaltyInfoResponse);
    }
}
