package ru.bmstu.loyaltyapp.service.converter;

import ru.bmstu.loyaltyapp.dto.LoyaltyDTO;
import ru.bmstu.loyaltyapp.dto.LoyaltyIntoResponse;
import ru.bmstu.loyaltyapp.dto.enums.StatusEnum;
import ru.bmstu.loyaltyapp.model.LoyaltyEntity;

public class LoyaltyConverter {
    public static LoyaltyDTO fromLoyaltyEntityToLoyaltyDTO(LoyaltyEntity loyaltyEntity) {
        return new LoyaltyDTO()
                .setId(loyaltyEntity.getId())
                .setUsername(loyaltyEntity.getUsername())
                .setReservationCount(loyaltyEntity.getReservationCount())
                .setStatus(StatusEnum.valueOf(loyaltyEntity.getStatus()))
                .setDiscount(loyaltyEntity.getDiscount());
    }

    public static LoyaltyEntity fromLoyaltyDTOToLoyaltyEntity(LoyaltyDTO loyaltyDTO) {
        return new LoyaltyEntity()
                .setId(loyaltyDTO.getId())
                .setUsername(loyaltyDTO.getUsername())
                .setReservationCount(loyaltyDTO.getReservationCount())
                .setStatus(loyaltyDTO.getStatus().name())
                .setDiscount(loyaltyDTO.getDiscount());
    }

    public static LoyaltyIntoResponse fromLoyaltyDTOToLoyaltyInfoResponse(LoyaltyDTO loyaltyDTO) {
        return new LoyaltyIntoResponse()
                .setStatus(loyaltyDTO.getStatus())
                .setDiscount(loyaltyDTO.getDiscount())
                .setReservationCount(loyaltyDTO.getReservationCount());
    }
}
