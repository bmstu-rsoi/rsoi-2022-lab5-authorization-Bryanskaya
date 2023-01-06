package ru.bmstu.reservationapp.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "reservation")
@Accessors(chain = true)
@NoArgsConstructor
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "reservation_uid", unique = true)
    private UUID reservationUid;

    @NonNull
    @Column(name = "username")
    private String username;

    @NonNull
    @Column(name = "payment_uid")
    private UUID paymentUid;

    @Column(name = "hotel_id")
    private Integer hotelId;

    @NonNull
    @Column(name = "status")
    private String status;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;
}
