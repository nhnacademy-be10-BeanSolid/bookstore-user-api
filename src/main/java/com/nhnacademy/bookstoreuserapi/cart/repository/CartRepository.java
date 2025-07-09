package com.nhnacademy.bookstoreuserapi.cart.repository;

import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByUser_UserId(String userId);
    Optional<Cart> findByUser_UserId(String userId);
    Optional<Cart> findByGuestUUID(String guestUUID);
}
