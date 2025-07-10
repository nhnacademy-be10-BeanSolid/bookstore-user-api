package com.nhnacademy.bookstoreuserapi.cart.repository;

import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByUser_UserId(String userId);
    Optional<Cart> findByOwnerTypeAndUser_UserId(OwnerType ownerType, String userId);
    Optional<Cart> findByOwnerTypeAndGuestUUID(OwnerType ownerType, String guestUUID);

    void deleteByOwnerTypeAndCreatedAtBefore(OwnerType ownerType, LocalDateTime dateTime);
    void deleteByOwnerTypeAndUpdatedAtBefore(OwnerType ownerType, LocalDateTime dateTime);
}
