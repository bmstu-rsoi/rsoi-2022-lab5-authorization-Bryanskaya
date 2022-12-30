package ru.bmstu.gateway.dto.converter;

import ru.bmstu.gateway.dto.PaymentDTO;
import ru.bmstu.gateway.dto.PaymentInfo;

public class PaymentConverter {
    public static PaymentInfo fromPaymentDTOToPaymentInfo(PaymentDTO paymentDTO) {
        return new PaymentInfo()
                .setStatus(paymentDTO.getStatus())
                .setPrice(paymentDTO.getPrice());
    }
}
