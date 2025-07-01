package com.nhnacademy.bookstoreuserapi.cart.repository;

import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import com.nhnacademy.bookstoreuserapi.cart.repository.queryfactory.CartRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {

    List<Cart> findAllByUser_UserId(String userId);


}
