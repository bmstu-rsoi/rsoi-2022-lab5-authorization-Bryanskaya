package ru.bmstu.paymentapp.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "payment")
@Accessors(chain = true)
@NoArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "payment_uid")
    private UUID paymentUid;

    @NonNull
    @Column(name = "status")
    private String status;

    @NonNull
    @Column(name = "price")
    private Integer price;
}
