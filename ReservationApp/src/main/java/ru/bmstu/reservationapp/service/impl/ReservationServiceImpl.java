package ru.bmstu.reservationapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bmstu.reservationapp.dto.ReservationDTO;
import ru.bmstu.reservationapp.model.ReservationEntity;
import ru.bmstu.reservationapp.repository.ReservationRepository;
import ru.bmstu.reservationapp.repository.TokenRepository;
import ru.bmstu.reservationapp.service.ReservationService;
import ru.bmstu.reservationapp.service.converter.ReservationConverter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private final TokenRepository tokenRepository;

    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<ReservationDTO> getReservationsByUsername(String bearerToken) {
        String username = tokenRepository.getUsername(bearerToken);

        return reservationRepository
                .getReservationsByUsername(username)
                .stream()
                .map(ReservationConverter::fromReservationEntityToReservationDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationDTO getReservationsByUsernameReservationUid(String bearerToken, UUID reservationUid) {
        String username = tokenRepository.getUsername(bearerToken);

        ReservationEntity reservationEntity = reservationRepository.getReservationsByUsernameReservationUid(username, reservationUid);
        return (reservationEntity == null) ? null : ReservationConverter.fromReservationEntityToReservationDTO(reservationEntity);
    }

    @Transactional
    public ReservationDTO postReservation(String bearerToken, ReservationDTO reservationDTO) {
        String username = tokenRepository.getUsername(bearerToken);

        ReservationEntity reservationEntity = reservationRepository.save(ReservationConverter
                    .fromReservationDTOTOReservationEntity(reservationDTO, username));
        return ReservationConverter.fromReservationEntityToReservationDTO(reservationEntity);
    }

    @Transactional
    public void revokeReservation(String bearerToken, UUID reservationUid) {
        String username = tokenRepository.getUsername(bearerToken);

        reservationRepository.revokeReservation(username, reservationUid);
    }
}
