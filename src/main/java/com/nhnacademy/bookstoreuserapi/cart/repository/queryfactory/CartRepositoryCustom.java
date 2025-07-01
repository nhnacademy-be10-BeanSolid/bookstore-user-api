package com.nhnacademy.bookstoreuserapi.cart.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.cart.domain.ResponseCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CartRepositoryCustom {
    Page<ResponseCart> findAllByUserId(String userId, Pageable pageable);

    ResponseCart findByUserIdAndBookId(String userId, long bookId);
}
