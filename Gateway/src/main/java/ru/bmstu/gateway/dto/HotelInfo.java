package ru.bmstu.gateway.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Data
@Validated
@Accessors(chain = true)
public class HotelInfo {
    private UUID hotelUid;
    private String name;
    private String fullAddress;
    private Integer stars;
}