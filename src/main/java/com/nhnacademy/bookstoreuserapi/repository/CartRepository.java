package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.Cart;
import com.nhnacademy.bookstoreuserapi.repository.queryfactory.CartRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {

    List<Cart> findAllByUser_UserId(String userId);


}
