package ru.bmstu.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bmstu.gateway.controller.exception.data.LoyaltyServiceUnauthorizedException;
import ru.bmstu.gateway.controller.exception.data.RelatedDataNotFoundException;
import ru.bmstu.gateway.controller.exception.data.ReservationByUsernameNotFoundException;
import ru.bmstu.gateway.controller.exception.data.ReservationByUsernameReservationUidNotFoundException;
import ru.bmstu.gateway.controller.exception.service.LoyaltyServiceNotAvailableException;
import ru.bmstu.gateway.dto.*;
import ru.bmstu.gateway.dto.converter.HotelInfoConverter;
import ru.bmstu.gateway.dto.converter.PaymentConverter;
import ru.bmstu.gateway.dto.converter.ReservationConverter;
import ru.bmstu.gateway.repository.HotelRepository;
import ru.bmstu.gateway.repository.LoyaltyRepository;
import ru.bmstu.gateway.repository.PaymentRepository;
import ru.bmstu.gateway.repository.ReservationRepository;

import java.util.ArrayList;
import java.util.UUID;

import static ru.bmstu.gateway.dto.converter.UserInfoResponseConverter.createUserInfoResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayService {
    private final LoyaltyRepository loyaltyRepository;
    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;


    public ResponseEntity<?> getHotels(Integer page, Integer size) {
        return hotelRepository.getHotels(page, size);
    }

    public UserInfoResponse getUserInfo(String username) {
        ArrayList<ReservationResponse> reservationResponseList = getReservationsList(username);

        LoyaltyInfoResponse loyaltyInfoResponse;
        try {
            loyaltyInfoResponse = getLoyaltyInfoResponseByUsername(username);
        } catch (LoyaltyServiceUnauthorizedException e) {
            throw e;
        } catch (Exception e)
        {
            log.info("----------- {}", e.getClass());
            loyaltyInfoResponse = new LoyaltyInfoResponse();
        }

        return createUserInfoResponse(reservationResponseList, loyaltyInfoResponse);
    }

    public LoyaltyInfoResponse getLoyaltyInfoResponseByUsername(String username) {
        return loyaltyRepository.getLoyaltyInfoResponseByUsername(username);
    }

    public ArrayList<ReservationResponse> getReservationsList(String username) {
        ReservationDTO[] reservationArr = reservationRepository.getReservationsArrByUsername(username);
        if (reservationArr == null)
            throw new ReservationByUsernameNotFoundException(username);

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

    public ReservationResponse getReservationByUsernameReservationUid(String username, UUID reservationUid) {
        ReservationDTO reservation = reservationRepository.getReservationByUsernameReservationUid(username, reservationUid);
        if (reservation == null)
            throw new ReservationByUsernameReservationUidNotFoundException(username, reservationUid);

        return _getReservationResponse(reservation);
    }

    public CreateReservationResponse postReservation(String username, CreateReservationRequest request) {
        HotelResponse hotelResponse = hotelRepository.getHotelResponseByHotelUid(request.getHotelUid());

        Integer reservationPrice = _getReservationPrice(username, request);
        PaymentDTO paymentDTO = paymentRepository.postPayment(reservationPrice);

        LoyaltyInfoResponse loyaltyInfoResponse;
        try {
            loyaltyInfoResponse = loyaltyRepository.updateReservationCount(username);
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
            reservationDTO = paymentRepository.postReservation(username, ReservationConverter.toReservationDTO(paymentDTO, request,
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

    private Integer _getReservationPrice(String username, CreateReservationRequest request) {
        Integer reservationPrice = reservationRepository.getReservationFullPrice(username, request);
        Integer discount = loyaltyRepository.getUserStatus(username);
        return loyaltyRepository.getReservationUpdatedPrice(reservationPrice, discount);
    }

    public void cancelReservation(String username, UUID reservationUid) {
        reservationRepository.cancelReservation(username, reservationUid);

        ReservationDTO reservationDTO = reservationRepository.getReservationByUsernameReservationUid(username, reservationUid);
        if (reservationDTO == null) return;

        paymentRepository.cancelPayment(reservationDTO.getPaymentUid());

        loyaltyRepository.cancelLoyalty(username);
    }
}

