package ru.bmstu.paymentapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bmstu.paymentapp.model.PaymentEntity;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    @Query(value = "SELECT * FROM payment WHERE payment_uid = ?1",
        nativeQuery = true)
    PaymentEntity getPaymentByUid(UUID paymentUid);

    @Modifying
    @Query(value = "UPDATE payment SET status = 'CANCELED' WHERE payment_uid = ?1",
        nativeQuery = true)
    void cancelPayment(UUID paymentUid);
}
