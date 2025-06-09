package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestReview;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import com.nhnacademy.bookstoreuserapi.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl {
    private final ReviewRepository reviewRepository;

    public ResponseReview addReview(SignUpRequestReview review){
        if(review == null || review.getUserId() == null || review.getBookId() <= 0) {
            throw new IllegalArgumentException("Invalid review data");
        }
        Review findReview = reviewRepository.findByUserIdByBookId(review.getUserId(), review.getBookId());
        if (findReview != null) {
            throw new IllegalArgumentException("Review already exists for user: " + review.getUserId() + " and book: " + review.getBookId());
        }
        Review savedReview = reviewRepository.save(new Review(review));
    }
}
