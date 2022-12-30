package ru.bmstu.gateway.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bmstu.gateway.config.AppParams;
import ru.bmstu.gateway.controller.exception.data.RelatedDataNotFoundException;
import ru.bmstu.gateway.controller.exception.data.RequestDataErrorException;
import ru.bmstu.gateway.controller.exception.data.ReservationByUsernameNotFoundException;
import ru.bmstu.gateway.controller.exception.data.ReservationByUsernameReservationUidNotFoundException;
import ru.bmstu.gateway.controller.exception.service.*;
import ru.bmstu.gateway.dto.*;
import ru.bmstu.gateway.dto.converter.HotelInfoConverter;
import ru.bmstu.gateway.dto.converter.PaymentConverter;
import ru.bmstu.gateway.dto.converter.ReservationConverter;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.UUID;

import static ru.bmstu.gateway.dto.converter.UserInfoResponseConverter.createUserInfoResponse;

@Slf4j
@RestController
@RequestMapping({"api/v1", ""})
public class GatewayController {
    @Resource
    private WebClient webClient;

    @Autowired
    private AppParams appParams;

    @GetMapping(value = "/manage/health", produces = "application/json")
    public ResponseEntity<?> isAlive() {
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping(value = "/hotels", produces = "application/json")
    public ResponseEntity<?> getHotels(@PathParam(value = "page") Integer page,
                                 @PathParam(value = "size") Integer size) {
        log.info(">>> GATEWAY: Request to get all hotels was caught (params: page={}, size={}).", page, size);

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostHotel)
                        .path(appParams.pathHotel + "/all")
                        .port(appParams.portHotel)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new HotelServiceNotAvailableException(error.statusCode().toString());
                })
                .toEntity(PaginationResponse.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    @GetMapping(value = "/me", produces = "application/json")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "X-User-Name") String username) {
        log.info(">>> GATEWAY: Request to get all reservations by current username={} was caught.", username);

        ArrayList<ReservationResponse> reservationResponseList = _getReservationsList(username);
        LoyaltyInfoResponse loyaltyInfoResponse = _getLoyaltyInfoResponseByUsername(username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createUserInfoResponse(reservationResponseList, loyaltyInfoResponse));

    }

    private LoyaltyInfoResponse _getLoyaltyInfoResponseByUsername(String username) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostLoyalty)
                        .path(appParams.pathLoyalty + "/user")
                        .port(appParams.portLoyalty)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-User-Name", username)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new LoyaltyServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(LoyaltyInfoResponse.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    @GetMapping(value = "/reservations", produces = "application/json")
    public ResponseEntity<?> getReservationsByUsername(@RequestHeader(value = "X-User-Name") String username) {
        log.info(">>> GATEWAY: Request to get all reservations by username={} was caught.", username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(_getReservationsList(username));
    }

    private ArrayList<ReservationResponse> _getReservationsList(String username) {
        ReservationDTO[] reservationArr = _getReservationsArrByUsername(username);
        if (reservationArr == null)
            throw new ReservationByUsernameNotFoundException(username);

        ArrayList<ReservationResponse> reservationResponseList = new ArrayList<>();
        for (ReservationDTO reservation: reservationArr)
            reservationResponseList.add(_getReservationResponse(reservation));

        return reservationResponseList;
    }

    @GetMapping(value = "/reservations/{reservationUid}", produces = "application/json")
    public ResponseEntity<?> getReservationByUsernameReservationUid(@RequestHeader(value = "X-User-Name") String username,
                                                                    @PathVariable(value = "reservationUid") UUID reservationUid) {
        log.info(">>> GATEWAY: Request to get all reservations by username={} and reservationUid={} was caught.", username, reservationUid);

        ReservationDTO reservation = _getReservationByUsernameReservationUid(username, reservationUid);
        if (reservation == null)
            throw new ReservationByUsernameReservationUidNotFoundException(username, reservationUid);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(_getReservationResponse(reservation));
    }

    private ReservationResponse _getReservationResponse(ReservationDTO reservationDTO) {
        HotelInfo hotelInfo = _getHotelInfoByHotelId(reservationDTO.getHotelId());
        PaymentInfo paymentInfo = _getPaymentInfoByPaymentUid(reservationDTO.getPaymentUid());
        if (hotelInfo == null || paymentInfo == null)
            throw new RelatedDataNotFoundException();

        return ReservationConverter.toReservationResponse(reservationDTO, hotelInfo, paymentInfo);
    }

    private ReservationDTO[] _getReservationsArrByUsername(String username) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostHotel)
                        .path(appParams.pathReservation)
                        .port(appParams.portHotel)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-User-Name", username)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new ReservationServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(ReservationDTO[].class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    private ReservationDTO _getReservationByUsernameReservationUid(String username, UUID reservationUid) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostHotel)
                        .path(appParams.pathReservation + "/{reservationUid}")
                        .port(appParams.portHotel)
                        .build(reservationUid))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-User-Name", username)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new ReservationServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(ReservationDTO.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    private HotelResponse _getHotelResponseByHotelId(Integer hotelId) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostHotel)
                        .path(appParams.pathHotel + "/{hotelId}")
                        .port(appParams.portHotel)
                        .build(hotelId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new HotelServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(HotelResponse.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    private HotelInfo _getHotelInfoByHotelId(Integer hotelId) {
        return HotelInfoConverter.
                fromHotelResponseToHotelInfo(_getHotelResponseByHotelId(hotelId));
    }

    private PaymentInfo _getPaymentInfoByPaymentUid(UUID paymentUid) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostPayment)
                        .path(appParams.pathPayment + "/{paymentUid}")
                        .port(appParams.portPayment)
                        .build(paymentUid))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new PaymentServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(PaymentInfo.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    @PostMapping(value = "/reservations")
    public ResponseEntity<CreateReservationResponse> postReservation(@RequestHeader(value = "X-User-Name") String username,
                                                                     @RequestBody CreateReservationRequest request) {
        log.info(">>> Request to create reservation was caught (username={}; data={}).", username, request.toString());

        if (!request.isValid())
            throw new RequestDataErrorException(request.toString());

        Integer reservationPrice = _getReservationPrice(username, request);
        PaymentDTO paymentDTO = _postPayment(reservationPrice);
        LoyaltyInfoResponse loyaltyInfoResponse = _updateReservationCount(username);
        ReservationDTO reservationDTO = _postReservation(username, ReservationConverter.toReservationDTO(paymentDTO, request,
                _getHotelIdByHotelUid(request.getHotelUid())));

        CreateReservationResponse createReservationResponse = ReservationConverter
                .fromReservationDTOToCreateReservationResponse(reservationDTO, request.getHotelUid(),
                        loyaltyInfoResponse.getDiscount(), PaymentConverter.fromPaymentDTOToPaymentInfo(paymentDTO));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createReservationResponse);
    }

    private Integer _getReservationPrice(String username, CreateReservationRequest request) {
        Integer reservationPrice = _getReservationFullPrice(username, request);
        Integer discount = _getUserStatus(username);
        return _getReservationUpdatedPrice(reservationPrice, discount);
    }

    private Integer _getReservationFullPrice(String username, CreateReservationRequest request) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostHotel)
                        .path(appParams.pathHotel + "/{hotelUid}/price")
                        .port(appParams.portHotel)
                        .queryParam("startDate", request.getStartDate())
                        .queryParam("endDate", request.getEndDate())
                        .build(request.getHotelUid()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-User-Name", username)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new ReservationServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(Integer.class)
                .onErrorMap(error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    private Integer _getUserStatus(String username) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostLoyalty)
                        .path(appParams.pathLoyalty)
                        .port(appParams.portLoyalty)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-User-Name", username)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new LoyaltyServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(Integer.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    private Integer _getReservationUpdatedPrice(Integer price, Integer discount) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostLoyalty)
                        .path(appParams.pathLoyalty + "/update")
                        .port(appParams.portLoyalty)
                        .queryParam("price", price)
                        .queryParam("discount", discount)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new LoyaltyServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(Integer.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    private PaymentDTO _postPayment(Integer price) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostPayment)
                        .path(appParams.pathPayment)
                        .port(appParams.portPayment)
                        .queryParam("price", price)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new PaymentServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(PaymentDTO.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    private LoyaltyInfoResponse _updateReservationCount(String username) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostLoyalty)
                        .path(appParams.pathLoyalty)
                        .port(appParams.portLoyalty)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-User-Name", username)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new LoyaltyServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(LoyaltyInfoResponse.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    private Integer _getHotelIdByHotelUid(UUID hotelUid) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostHotel)
                        .path(appParams.pathHotel + "/{hotelUid}/id")
                        .port(appParams.portHotel)
                        .build(hotelUid))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new HotelServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(Integer.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    private ReservationDTO _postReservation(String username, ReservationDTO reservationDTO) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostHotel)
                        .path(appParams.pathReservation)
                        .port(appParams.portHotel)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-User-Name", username)
                .body(BodyInserters.fromValue(reservationDTO))
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new PaymentServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(ReservationDTO.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/reservations/{reservationUid}", produces = "application/json")
    public void cancelReservation(@RequestHeader(value = "X-User-Name") String username,
                                  @PathVariable(value = "reservationUid") UUID reservationUid) {
        log.info(">>> GATEWAY: Request to delete reservation was caught (username={}; reservationUid={}).", username, reservationUid);

        _cancelReservation(username, reservationUid);
        ReservationDTO reservationDTO = _getReservationByUsernameReservationUid(username, reservationUid);
        if (reservationDTO == null) return;

        _cancelPayment(reservationDTO.getPaymentUid());
        LoyaltyInfoResponse loyaltyInfoResponse = _cancelLoyalty(username);
    }

    private void _cancelReservation(String username, UUID reservationUid) {
        webClient
            .delete()
            .uri(uriBuilder -> uriBuilder
                    .host(appParams.hostHotel)
                    .path(appParams.pathReservation + "/{reservationUid}")
                    .port(appParams.portHotel)
                    .build(reservationUid))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("X-User-Name", username)
            .retrieve()
            .onStatus(HttpStatus::isError, error -> {
                throw new ReservationServiceNotAvailableException(error.statusCode().toString());
            })
            .bodyToMono(Void.class)
            .onErrorMap(Throwable.class, error -> {
                throw new GatewayErrorException(error.getMessage());
            })
            .block();
    }

    private void _cancelPayment(UUID paymentUid) {
        webClient
            .delete()
            .uri(uriBuilder -> uriBuilder
                    .host(appParams.hostPayment)
                    .path(appParams.pathPayment + "/{paymentUid}")
                    .port(appParams.portPayment)
                    .build(paymentUid))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .onStatus(HttpStatus::isError, error -> {
                throw new PaymentServiceNotAvailableException(error.statusCode().toString());
            })
            .bodyToMono(Void.class)
            .onErrorMap(Throwable.class, error -> {
                throw new GatewayErrorException(error.getMessage());
            })
            .block();
    }

    private LoyaltyInfoResponse _cancelLoyalty(String username) {
        return webClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostLoyalty)
                        .path(appParams.pathLoyalty)
                        .port(appParams.portLoyalty)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-User-Name", username)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new LoyaltyServiceNotAvailableException(error.statusCode().toString());
                })
                .bodyToMono(LoyaltyInfoResponse.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    @GetMapping(value = "/loyalty", produces = "application/json")
    public ResponseEntity<?> getLoyaltyInfoResponseByUsername(@RequestHeader(value = "X-User-Name") String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(_getLoyaltyInfoResponseByUsername(username));
    }
}
