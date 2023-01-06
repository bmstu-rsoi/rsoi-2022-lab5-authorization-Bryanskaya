package ru.bmstu.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bmstu.gateway.controller.exception.data.token.UnauthorizedException;
import ru.bmstu.gateway.controller.exception.data.RelatedDataNotFoundException;
import ru.bmstu.gateway.controller.exception.data.ReservationByUsernameNotFoundException;
import ru.bmstu.gateway.controller.exception.data.ReservationByUsernameReservationUidNotFoundException;
import ru.bmstu.gateway.controller.exception.service.LoyaltyServiceNotAvailableException;
import ru.bmstu.gateway.dto.*;
import ru.bmstu.gateway.dto.converter.HotelInfoConverter;
import ru.bmstu.gateway.dto.converter.PaymentConverter;
import ru.bmstu.gateway.dto.converter.ReservationConverter;
import ru.bmstu.gateway.repository.*;

import java.util.ArrayList;
import java.util.UUID;

import static ru.bmstu.gateway.dto.converter.UserInfoResponseConverter.createUserInfoResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayService {
    @Autowired
    private final TokenRepository tokenRepository;

    private final LoyaltyRepository loyaltyRepository;
    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;


    public ResponseEntity<?> getHotels(Integer page, Integer size) {
        return hotelRepository.getHotels(page, size);
    }

    public UserInfoResponse getUserInfo(String bearerToken) {
        ArrayList<ReservationResponse> reservationResponseList = getReservationsList(bearerToken);

        LoyaltyInfoResponse loyaltyInfoResponse;
        try {
            loyaltyInfoResponse = getLoyaltyInfoResponseByUsername(bearerToken);
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e)
        {
            loyaltyInfoResponse = new LoyaltyInfoResponse();
        }

        return createUserInfoResponse(reservationResponseList, loyaltyInfoResponse);
    }

    public LoyaltyInfoResponse getLoyaltyInfoResponseByUsername(String bearerToken) {
        return loyaltyRepository.getLoyaltyInfoResponseByUsername(bearerToken);
    }

    public ArrayList<ReservationResponse> getReservationsList(String bearerToken) {
        ReservationDTO[] reservationArr = reservationRepository.getReservationsArrByUsername(bearerToken);
        if (reservationArr == null)
            throw new ReservationByUsernameNotFoundException(tokenRepository.getUsername(bearerToken));

        ArrayList<ReservationResponse> reservationResponseList = new ArrayList<>();
        for (ReservationDTO reservation: reservationArr)
            reservationResponseList.add(_getReservationResponse(reservation));

        return reservationResponseList;
    }

    private ReservationResponse _getReservationResponse(ReservationDTO reservationDTO) {
        HotelInfo hotelInfo = _getHotelInfoByHotelId(reservationDTO.getHotelId());
        if (hotelInfo == null)
            throw new RelatedDataNotFoundException();

        PaymentInfo paymentInfo;
        try {
            paymentInfo = paymentRepository.getPaymentInfoByPaymentUid(reservationDTO.getPaymentUid());
        } catch (Exception e) {
            paymentInfo = null;
        }

        return ReservationConverter.toReservationResponse(reservationDTO, hotelInfo, paymentInfo);
    }

    private HotelInfo _getHotelInfoByHotelId(Integer hotelId) {
        return HotelInfoConverter.
                fromHotelResponseToHotelInfo(hotelRepository.getHotelResponseByHotelId(hotelId));
    }

    public ReservationResponse getReservationByUsernameReservationUid(String bearerToken, UUID reservationUid) {
        ReservationDTO reservation = reservationRepository.getReservationByUsernameReservationUid(bearerToken, reservationUid);
        if (reservation == null)
            throw new ReservationByUsernameReservationUidNotFoundException(tokenRepository.getUsername(bearerToken),
                                                                            reservationUid);

        return _getReservationResponse(reservation);
    }

    public CreateReservationResponse postReservation(String bearerToken, CreateReservationRequest request) {
        HotelResponse hotelResponse = hotelRepository.getHotelResponseByHotelUid(request.getHotelUid());

        Integer reservationPrice = _getReservationPrice(bearerToken, request);
        PaymentDTO paymentDTO = paymentRepository.postPayment(reservationPrice);

        LoyaltyInfoResponse loyaltyInfoResponse;
        try {
            loyaltyInfoResponse = loyaltyRepository.updateReservationCount(bearerToken);
        } catch (LoyaltyServiceNotAvailableException e1) {
            loyaltyInfoResponse = null;
        } catch (Exception e1) {
            log.error(">>> GATEWAY: update loyalty status operation was failed, err={}", e1.getMessage());

            try {
                paymentRepository.cancelPayment(paymentDTO.getPaymentUid());
            } catch (Exception e2) {
                log.error(">>> GATEWAY: err={}", e2.getMessage());
            }

            throw new LoyaltyServiceNotAvailableException(HttpStatus.SERVICE_UNAVAILABLE);
        }

        ReservationDTO reservationDTO;
        try {
            reservationDTO = paymentRepository.postReservation(bearerToken, ReservationConverter.toReservationDTO(paymentDTO, request,
                    hotelRepository.getHotelIdByHotelUid(request.getHotelUid())));
        } catch (Exception e) {
            reservationDTO = null;
        }

        return ReservationConverter
                .fromReservationDTOToCreateReservationResponse(reservationDTO,
                        request.getHotelUid(),
                        loyaltyInfoResponse.getDiscount(),
                        PaymentConverter.fromPaymentDTOToPaymentInfo(paymentDTO));
    }

    private Integer _getReservationPrice(String bearerToken, CreateReservationRequest request) {
        Integer reservationPrice = reservationRepository.getReservationFullPrice(bearerToken, request);
        Integer discount = loyaltyRepository.getUserStatus(bearerToken);
        return loyaltyRepository.getReservationUpdatedPrice(reservationPrice, discount);
    }

    public void cancelReservation(String bearerToken, UUID reservationUid) {
        reservationRepository.cancelReservation(bearerToken, reservationUid);

        ReservationDTO reservationDTO = reservationRepository.getReservationByUsernameReservationUid(bearerToken,
                                                    reservationUid);
        if (reservationDTO == null) return;

        paymentRepository.cancelPayment(reservationDTO.getPaymentUid());

        loyaltyRepository.cancelLoyalty(bearerToken);
    }
}

