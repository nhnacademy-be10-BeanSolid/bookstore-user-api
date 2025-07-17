package com.nhnacademy.bookstoreuserapi.review.service;

import com.nhnacademy.bookstoreuserapi.review.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ReviewService {

    ResponseReview addReview(String userId, ReviewCreateRequest review);

    ResponseReview editReview(String userId, long reviewId, ReviewUpdateRequest review);

    ResponseReview getReview(long reviewId);

    Page<ResponseSimpleReviewByUser> getReviewsByUserId(String userId, Pageable pageable);

    Page<ResponseSimpleReview> getReviewsByBookId(long bookId, Pageable pageable);

    long countReviewsByBookId(long bookId);

    double getAverageEvaluationScoreByBookId(long bookId);

}
