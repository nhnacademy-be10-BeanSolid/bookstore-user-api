package com.nhnacademy.bookstoreuserapi.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<ResponseReview> findAllByUserId(String userId, Pageable pageable);
    Page<ResponseReview> findAllByBookId(long bookId, Pageable pageable);
}
