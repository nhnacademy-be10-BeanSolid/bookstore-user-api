package com.nhnacademy.bookstoreuserapi.review.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.review.domain.ResponseSimpleReview;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseSimpleReviewByUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<ResponseSimpleReviewByUser> findAllByUserId(String userId, Pageable pageable);
    Page<ResponseSimpleReview> findAllByBookId(long bookId, Pageable pageable);

    double averageEvaluationScoreByBookId(long bookId);
}
