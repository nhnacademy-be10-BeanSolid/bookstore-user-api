package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.ReviewUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import java.util.List;

public interface ReviewService {

    ResponseReview addReview(ReviewCreateRequest review);

    ResponseReview editReview(long reviewId, ReviewUpdateRequest review);

    ResponseReview getReview(long reviewId);

    List<ResponseReview> getReviewsByUserId(String userId);

    List<ResponseReview> getReviewsByBookId(long bookId);

}
