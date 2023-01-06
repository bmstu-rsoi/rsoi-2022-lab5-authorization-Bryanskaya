package ru.bmstu.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class PaginationResponse implements Serializable {
    @JsonProperty(value = "page")
    private Integer page;

    @JsonProperty(value = "pageSize")
    private Integer pageSize;

    @JsonProperty(value = "totalElements")
    private Long totalElements;

    @JsonProperty(value = "items")
    private List<HotelResponse> items;
}
