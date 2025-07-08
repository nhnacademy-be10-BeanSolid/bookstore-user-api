package com.nhnacademy.bookstoreuserapi.cart.repository;

import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import com.nhnacademy.bookstoreuserapi.cart.repository.queryfactory.CartRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {
    boolean existsByUserIdAndOwnerType(String userId, OwnerType ownerType);
}
