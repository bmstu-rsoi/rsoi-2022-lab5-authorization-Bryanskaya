package ru.bmstu.reservationapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "hotels")
@Accessors(chain = true)
@NoArgsConstructor
public class HotelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "hotel_uid", unique = true)
    private UUID hotelUid;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "country")
    private String country;

    @NonNull
    @Column(name = "city")
    private String city;

    @NonNull
    @Column(name = "address")
    private String address;

    @Column(name = "stars")
    private Integer stars;

    @NonNull
    @Column(name = "price")
    private Integer price;
}
