package ru.bmstu.gateway.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;
import ru.bmstu.gateway.dto.enums.ReservationStatusEnum;

@Data
@Validated
@Accessors(chain = true)
public class PaymentInfo {
    private ReservationStatusEnum status;
    private Integer price;
}
