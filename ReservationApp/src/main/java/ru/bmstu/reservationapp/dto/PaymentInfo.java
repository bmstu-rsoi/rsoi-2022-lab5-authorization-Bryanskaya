package ru.bmstu.reservationapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;
import ru.bmstu.reservationapp.dto.enums.StatusEnum;

@Data
@Validated
@Accessors(chain = true)
public class PaymentInfo {
    private StatusEnum status;
    private Integer price;
}
