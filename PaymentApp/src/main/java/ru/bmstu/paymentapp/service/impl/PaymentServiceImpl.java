package ru.bmstu.paymentapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bmstu.paymentapp.dto.PaymentDTO;
import ru.bmstu.paymentapp.repository.PaymentRepository;
import ru.bmstu.paymentapp.service.PaymentService;

import java.util.UUID;

import static ru.bmstu.paymentapp.service.converter.PaymentConverter.createPaymentEntity;
import static ru.bmstu.paymentapp.service.converter.PaymentConverter.fromPaymentEntityToPaymentDTO;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public PaymentDTO getPaymentByUid(UUID paymentUid) {
        return fromPaymentEntityToPaymentDTO(paymentRepository.getPaymentByUid(paymentUid));
    }

    @Transactional
    public PaymentDTO postPayment(Integer price) {
        return fromPaymentEntityToPaymentDTO(paymentRepository.save(createPaymentEntity(price)));
    }

    @Transactional
    public void cancelPayment(UUID paymentUid) {
        paymentRepository.cancelPayment(paymentUid);
    }
}
