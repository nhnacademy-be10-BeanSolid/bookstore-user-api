package com.nhnacademy.bookstoreuserapi.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.domain.response.ResponseCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CartRepositoryCustom {
    Page<ResponseCart> findAllByUserId(String userId, Pageable pageable);

    ResponseCart findByUserIdAndBookId(String userId, long bookId);
}
