package ru.bmstu.reservationapp.service.converter;

import ru.bmstu.reservationapp.dto.HotelResponse;
import ru.bmstu.reservationapp.model.HotelEntity;

public class HotelConverter {
    public static HotelResponse fromHotelsEntityToHotelResponse(HotelEntity hotelEntity) {
        return new HotelResponse()
                .setHotelUid(hotelEntity.getHotelUid())
                .setName(hotelEntity.getName())
                .setCountry(hotelEntity.getCountry())
                .setCity(hotelEntity.getCity())
                .setAddress(hotelEntity.getAddress())
                .setStars(hotelEntity.getStars())
                .setPrice(hotelEntity.getPrice());
    }
}
