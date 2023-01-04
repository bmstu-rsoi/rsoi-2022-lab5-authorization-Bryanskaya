package ru.bmstu.gateway.repository;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import ru.bmstu.gateway.controller.exception.service.GatewayErrorException;
import ru.bmstu.gateway.controller.exception.service.PaymentServiceNotAvailableException;
import ru.bmstu.gateway.dto.PaymentDTO;
import ru.bmstu.gateway.dto.PaymentInfo;
import ru.bmstu.gateway.dto.ReservationDTO;

import java.util.UUID;

@Slf4j
@Repository
public class PaymentRepository extends BaseRepository {
    public PaymentInfo getPaymentInfoByPaymentUid(UUID paymentUid) {
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
                    throw new PaymentServiceNotAvailableException(error.statusCode());
                })
                .bodyToMono(PaymentInfo.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    public PaymentDTO postPayment(Integer price) {
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
                    throw new PaymentServiceNotAvailableException(error.statusCode());
                })
                .bodyToMono(PaymentDTO.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    public ReservationDTO postReservation(String bearerToken, ReservationDTO reservationDTO) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostHotel)
                        .path(appParams.pathReservation)
                        .port(appParams.portHotel)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", bearerToken)
                .body(BodyInserters.fromValue(reservationDTO))
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new PaymentServiceNotAvailableException(error.statusCode());
                })
                .bodyToMono(ReservationDTO.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    public void cancelPayment(UUID paymentUid) {
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
                    throw new PaymentServiceNotAvailableException(error.statusCode());
                })
                .bodyToMono(Void.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }
}
