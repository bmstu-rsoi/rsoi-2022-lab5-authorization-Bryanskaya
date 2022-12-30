package ru.bmstu.gateway.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;
import ru.bmstu.gateway.dto.enums.ReservationStatusEnum;

import java.util.UUID;

@Data
@Validated
@Accessors(chain = true)
public class PaymentDTO {
    private UUID paymentUid;
    private ReservationStatusEnum status;
    private Integer price;
}
