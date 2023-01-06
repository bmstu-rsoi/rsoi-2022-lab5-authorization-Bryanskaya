package ru.bmstu.gateway.dto.converter;

import ru.bmstu.gateway.dto.HotelInfo;
import ru.bmstu.gateway.dto.HotelResponse;

public class HotelInfoConverter {
    public static HotelInfo fromHotelResponseToHotelInfo(HotelResponse hotelResponse) {
        return new HotelInfo()
                .setHotelUid(hotelResponse.getHotelUid())
                .setName(hotelResponse.getName())
                .setFullAddress(String.format("%s, %s, %s", hotelResponse.getCountry(), hotelResponse.getCity(), hotelResponse.getAddress()))
                .setStars(hotelResponse.getStars());
    }
}
