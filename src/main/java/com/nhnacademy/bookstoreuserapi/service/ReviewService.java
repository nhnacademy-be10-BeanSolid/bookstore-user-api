package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.ReviewUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ReviewService {

    ResponseReview addReview(String userId, ReviewCreateRequest review);

    ResponseReview editReview(String userId, long reviewId, ReviewUpdateRequest review);

    ResponseReview getReview(long reviewId);

    Page<ResponseReview> getReviewsByUserId(String userId, Pageable pageable);

    Page<ResponseReview> getReviewsByBookId(long bookId, Pageable pageable);

}
