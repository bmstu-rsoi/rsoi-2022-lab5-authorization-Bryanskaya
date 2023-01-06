package ru.bmstu.reservationapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bmstu.reservationapp.model.ReservationEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
    @Query(value = "SELECT * FROM reservation WHERE username = ?1",
            nativeQuery = true)
    List<ReservationEntity> getReservationsByUsername(String username);

    @Query(value = "SELECT * FROM reservation WHERE username = ?1 and reservation_uid = ?2",
            nativeQuery = true)
    ReservationEntity getReservationsByUsernameReservationUid(String username, UUID reservationUid);

    @Modifying
    @Query(value = "UPDATE reservation SET status = 'CANCELED' WHERE username = ?1 and reservation_uid = ?2",
            nativeQuery = true)
    void revokeReservation(String username, UUID reservationUid);
}
