package ru.bmstu.loyaltyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bmstu.loyaltyapp.model.LoyaltyEntity;

public interface LoyaltyRepository extends JpaRepository<LoyaltyEntity, Integer> {
    @Query(value = "SELECT discount FROM loyalty WHERE username = ?1",
        nativeQuery = true)
    Integer getDiscountByUsername(String username);

    @Query(value = "SELECT * FROM loyalty WHERE username = ?1",
        nativeQuery = true)
    LoyaltyEntity getLoyaltyInfoResponseByUsername(String username);

    @Query(value = "SELECT * FROM loyalty WHERE username = ?1",
        nativeQuery = true)
    LoyaltyEntity getLoyaltyEntityByUsername(String username);
}
