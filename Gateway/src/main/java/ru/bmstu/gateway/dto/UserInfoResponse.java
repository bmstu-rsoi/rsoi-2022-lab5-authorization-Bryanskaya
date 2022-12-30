package ru.bmstu.gateway.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;

@Data
@Validated
@Accessors(chain = true)
public class UserInfoResponse {
    private ArrayList<ReservationResponse> reservations;
    private LoyaltyInfoResponse loyalty;
}
