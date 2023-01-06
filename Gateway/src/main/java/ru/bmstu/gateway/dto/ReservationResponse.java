package ru.bmstu.gateway.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;
import ru.bmstu.gateway.dto.enums.ReservationStatusEnum;

import java.sql.Date;
import java.util.UUID;

@Data
@Validated
@Accessors(chain = true)
public class ReservationResponse {
    private UUID reservationUid;
    private HotelInfo hotel;
    private Date startDate;
    private Date endDate;
    private ReservationStatusEnum status;
    private PaymentInfo payment;
}
