package ru.bmstu.gateway.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import ru.bmstu.gateway.controller.exception.data.token.UnauthorizedException;
import ru.bmstu.gateway.controller.exception.service.GatewayErrorException;
import ru.bmstu.gateway.controller.exception.service.LoyaltyServiceNotAvailableException;
import ru.bmstu.gateway.dto.LoyaltyInfoResponse;

@Slf4j
@Repository
public class LoyaltyRepository extends BaseRepository {
    public LoyaltyInfoResponse getLoyaltyInfoResponseByUsername(String bearerToken) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostLoyalty)
                        .path(appParams.pathLoyalty + "/user")
                        .port(appParams.portLoyalty)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", bearerToken)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, error -> {
                    throw new UnauthorizedException(error.statusCode());
                })
                .onStatus(HttpStatus::isError, error -> {
                    throw new LoyaltyServiceNotAvailableException(error.statusCode());
                })
                .bodyToMono(LoyaltyInfoResponse.class)
                .block();
    }

    public Integer getUserStatus(String bearerToken) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostLoyalty)
                        .path(appParams.pathLoyalty)
                        .port(appParams.portLoyalty)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", bearerToken)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new LoyaltyServiceNotAvailableException(error.statusCode());
                })
                .bodyToMono(Integer.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    public Integer getReservationUpdatedPrice(Integer price, Integer discount) {
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
                    throw new LoyaltyServiceNotAvailableException(error.statusCode());
                })
                .bodyToMono(Integer.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    public LoyaltyInfoResponse updateReservationCount(String bearerToken) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostLoyalty)
                        .path(appParams.pathLoyalty)
                        .port(appParams.portLoyalty)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", bearerToken)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new LoyaltyServiceNotAvailableException(error.statusCode());
                })
                .bodyToMono(LoyaltyInfoResponse.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }

    public LoyaltyInfoResponse cancelLoyalty(String bearerToken) {
        return webClient
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .host(appParams.hostLoyalty)
                        .path(appParams.pathLoyalty)
                        .port(appParams.portLoyalty)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", bearerToken)
                .retrieve()
                .onStatus(HttpStatus::isError, error -> {
                    throw new LoyaltyServiceNotAvailableException(error.statusCode());
                })
                .bodyToMono(LoyaltyInfoResponse.class)
                .onErrorMap(Throwable.class, error -> {
                    throw new GatewayErrorException(error.getMessage());
                })
                .block();
    }
}
