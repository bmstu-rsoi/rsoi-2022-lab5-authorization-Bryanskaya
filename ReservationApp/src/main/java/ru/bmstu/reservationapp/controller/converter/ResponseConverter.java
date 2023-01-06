package ru.bmstu.reservationapp.controller.converter;

import org.springframework.data.domain.Page;
import ru.bmstu.reservationapp.dto.HotelResponse;
import ru.bmstu.reservationapp.dto.PaginationResponse;

public class ResponseConverter {
    public static PaginationResponse toPaginationResponse(Integer page, Integer pageSize,
                                                              Page<HotelResponse> hotelResponsePage) {
        return new PaginationResponse()
                .setPage(page)
                .setPageSize(pageSize)
                .setTotalElements(hotelResponsePage.getTotalElements())
                .setItems(hotelResponsePage.getContent());
    }
}
