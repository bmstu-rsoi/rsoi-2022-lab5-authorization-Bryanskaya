package ru.bmstu.reservationapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.reservationapp.controller.converter.ResponseConverter;
import ru.bmstu.reservationapp.dto.HotelResponse;
import ru.bmstu.reservationapp.dto.PaginationResponse;
import ru.bmstu.reservationapp.service.HotelService;

import javax.websocket.server.PathParam;
import java.sql.Date;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/hotels")
public class HotelController {
    private final HotelService hotelService;


    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<PaginationResponse> getHotels(@PathParam(value = "page") Integer page,
                                                     @PathParam(value = "size") Integer size) {
        log.info(">>> RESERVATION: Request to get all hotels was caught.");

        Pageable paging = PageRequest.of(page - 1, size);
        Page<HotelResponse> hotelsResponsePage = hotelService.getHotels(paging);

        PaginationResponse paginationResponse = ResponseConverter.toPaginationResponse(page, size, hotelsResponsePage);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paginationResponse);
    }

    @GetMapping(value = "/{hotelId}", produces = "application/json")
    public ResponseEntity<HotelResponse> getHotelByHotelId(@PathVariable Integer hotelId) {
        log.info(">>> RESERVATION: Request to get hotel by hotelId={} was caught.", hotelId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hotelService.getHotelByHotelId(hotelId));
    }


    @GetMapping(value = "/exists/{hotelUid}", produces = "application/json")
    public ResponseEntity<HotelResponse> getHotelByUid(@PathVariable UUID hotelUid) {
        log.info(">>> RESERVATION: Request to get hotel by hotelUid={} was caught.", hotelUid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hotelService.getHotelByHotelUid(hotelUid));
    }


    @GetMapping(value = "/{hotelUid}/id", produces = "application/json")
    public ResponseEntity<?> getHotelIdByHotelUid(@PathVariable UUID hotelUid) {
        log.info(">>> RESERVATION: Request to get hotelId by hotelUid={} was caught.", hotelUid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hotelService.getHotelIdByHotelUid(hotelUid));
    }


    @GetMapping(value = "/{hotelUid}/price", produces = "application/json")
    public ResponseEntity<Integer> getHotelDatePrice(@PathVariable UUID hotelUid,
                                                     @PathParam(value = "startDate") Date startDate,
                                                     @PathParam(value = "endDate") Date endDate) {
        log.info(">>> Request to get reservation's price was caught, hotelUid={}, startDate={}; endDate={}).",
                hotelUid, startDate, endDate);

        Integer price = hotelService.getHotelDatePrice(hotelUid, startDate, endDate);
        HttpStatus httpStatus = (price == null) ? HttpStatus.NOT_FOUND : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .body(price);
    }
}
