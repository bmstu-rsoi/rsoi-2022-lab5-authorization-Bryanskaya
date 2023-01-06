package ru.bmstu.reservationapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@Accessors(chain = true)
public class PaginationResponse {
    private Integer page;
    private Integer pageSize;
    private Long totalElements;
    private List<HotelResponse> items;
}
