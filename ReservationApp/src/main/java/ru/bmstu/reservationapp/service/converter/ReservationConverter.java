package ru.bmstu.reservationapp.service.converter;

import ru.bmstu.reservationapp.dto.ReservationDTO;
import ru.bmstu.reservationapp.dto.enums.StatusEnum;
import ru.bmstu.reservationapp.model.ReservationEntity;


public class ReservationConverter {
    public static ReservationDTO fromReservationEntityToReservationDTO(ReservationEntity reservationEntity) {
        return new ReservationDTO()
                .setReservationUid(reservationEntity.getReservationUid())
                .setHotelId(reservationEntity.getHotelId())
                .setStartDate(reservationEntity.getStartDate())
                .setEndDate(reservationEntity.getEndDate())
                .setStatus(StatusEnum.valueOf(reservationEntity.getStatus()))
                .setPaymentUid(reservationEntity.getPaymentUid());
    }

    public static ReservationEntity fromReservationDTOTOReservationEntity(ReservationDTO reservationDTO, String username) {
        return new ReservationEntity()
                .setReservationUid(reservationDTO.getReservationUid())
                .setUsername(username)
                .setPaymentUid(reservationDTO.getPaymentUid())
                .setHotelId(reservationDTO.getHotelId())
                .setStatus(reservationDTO.getStatus().toString())
                .setStartDate(reservationDTO.getStartDate())
                .setEndDate(reservationDTO.getEndDate());
    }
}
