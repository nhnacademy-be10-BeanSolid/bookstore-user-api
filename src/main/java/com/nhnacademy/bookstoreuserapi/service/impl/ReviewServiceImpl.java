package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestReview;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestReview;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import com.nhnacademy.bookstoreuserapi.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

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
        return new ResponseReview(
                savedReview.getReviewId(),
                savedReview.getEvaluationScore(),
                savedReview.getReviewContent(),
                savedReview.getReviewPhoto(),
                savedReview.getReviewedAt(),
                savedReview.getUpdatedAt(),
                savedReview.getUserId(),
                savedReview.getBookId()
        );
    }

    public ResponseReview editReview(long reviewId, EditRequestReview review) {
        Review findReview = reviewRepository.findById(reviewId).orElse(null);
        if (findReview == null) {
            throw new IllegalArgumentException("Review not found with ID: " + reviewId);
        }
        findReview.setEvaluationScore(review.getEvaluationScore());
        findReview.setReviewContent(review.getReviewContent());
        findReview.setReviewPhoto(review.getReviewPhoto());
        findReview.setUpdatedAt(ZonedDateTime.now());

        Review updatedReview = reviewRepository.save(findReview);
        return new ResponseReview(
                updatedReview.getReviewId(),
                updatedReview.getEvaluationScore(),
                updatedReview.getReviewContent(),
                updatedReview.getReviewPhoto(),
                updatedReview.getReviewedAt(),
                updatedReview.getUpdatedAt(),
                updatedReview.getUserId(),
                updatedReview.getBookId()
        );
    }

    public ResponseReview getReview(long reviewId) {
        Review findReview = reviewRepository.findById(reviewId).orElse(null);
        if (findReview == null) {
            throw new IllegalArgumentException("Review not found with ID: " + reviewId);
        }
        return new ResponseReview(
                findReview.getReviewId(),
                findReview.getEvaluationScore(),
                findReview.getReviewContent(),
                findReview.getReviewPhoto(),
                findReview.getReviewedAt(),
                findReview.getUpdatedAt(),
                findReview.getUserId(),
                findReview.getBookId()
        );
    }

    public List<ResponseReview> getReviewsByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        List<Review> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream()
                .map(review -> new ResponseReview(
                        review.getReviewId(),
                        review.getEvaluationScore(),
                        review.getReviewContent(),
                        review.getReviewPhoto(),
                        review.getReviewedAt(),
                        review.getUpdatedAt(),
                        review.getUserId(),
                        review.getBookId()))
                .toList();
    }

    public List<ResponseReview> getReviewsByBookId(long bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book ID");
        }
        List<Review> reviews = reviewRepository.findAllByBookId(bookId);
        return reviews.stream()
                .map(review -> new ResponseReview(
                        review.getReviewId(),
                        review.getEvaluationScore(),
                        review.getReviewContent(),
                        review.getReviewPhoto(),
                        review.getReviewedAt(),
                        review.getUpdatedAt(),
                        review.getUserId(),
                        review.getBookId()))
                .toList();
    }

}
