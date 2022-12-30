package ru.bmstu.paymentapp.service;

import ru.bmstu.paymentapp.dto.PaymentDTO;

import java.util.UUID;

public interface PaymentService {
    PaymentDTO getPaymentByUid(UUID paymentUid);
    PaymentDTO postPayment(Integer price);
    void cancelPayment(UUID paymentUid);
}
