package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import com.nhnacademy.bookstoreuserapi.exception.*;
import com.nhnacademy.bookstoreuserapi.repository.ReviewRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseReview addReview(ReviewCreateRequest review){
        Review findReview = reviewRepository.findByUser_UserIdAndBookId(review.userId(), review.bookId());
        if (findReview != null) {
            throw new ReviewAlreadyExistsBookException(review.userId(), review.bookId());
        }
        User user = userRepository.findById(review.userId())
                .orElseThrow(() -> new UserNotFoundException(review.userId()));
        Review savedReview = reviewRepository.save(new Review(review, user));
        return new ResponseReview(
                savedReview.getReviewId(),
                savedReview.getEvaluationScore(),
                savedReview.getReviewContent(),
                savedReview.getReviewPhoto(),
                savedReview.getReviewedAt(),
                savedReview.getUpdatedAt(),
                savedReview.getUser().getUserId(),
                savedReview.getBookId()
        );
    }

    @Override
    public ResponseReview editReview(long reviewId, ReviewUpdateRequest review) {
        Review findReview = reviewRepository.findById(reviewId).orElse(null);
        if (findReview == null) {
            throw new ReviewNotFoundException(reviewId);
        }
        findReview.setEvaluationScore(review.evaluationScore());
        findReview.setReviewContent(review.reviewContent());
        findReview.setReviewPhoto(review.reviewPhoto());
        findReview.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(findReview);
        return new ResponseReview(
                updatedReview.getReviewId(),
                updatedReview.getEvaluationScore(),
                updatedReview.getReviewContent(),
                updatedReview.getReviewPhoto(),
                updatedReview.getReviewedAt(),
                updatedReview.getUpdatedAt(),
                updatedReview.getUser().getUserId(),
                updatedReview.getBookId()
        );
    }

    @Override
    public ResponseReview getReview(long reviewId) {
        Review findReview = reviewRepository.findById(reviewId).orElse(null);
        if (findReview == null) {
            throw new ReviewNotFoundException(reviewId);
        }
        return new ResponseReview(
                findReview.getReviewId(),
                findReview.getEvaluationScore(),
                findReview.getReviewContent(),
                findReview.getReviewPhoto(),
                findReview.getReviewedAt(),
                findReview.getUpdatedAt(),
                findReview.getUser().getUserId(),
                findReview.getBookId()
        );
    }

    @Override
    public List<ResponseReview> getReviewsByUserId(String userId) {
        List<Review> reviews = reviewRepository.findAllByUser_UserId(userId);
        return reviews.stream()
                .map(review -> new ResponseReview(
                        review.getReviewId(),
                        review.getEvaluationScore(),
                        review.getReviewContent(),
                        review.getReviewPhoto(),
                        review.getReviewedAt(),
                        review.getUpdatedAt(),
                        review.getUser().getUserId(),
                        review.getBookId()))
                .toList();
    }

    @Override
    public List<ResponseReview> getReviewsByBookId(long bookId) {
        List<Review> reviews = reviewRepository.findAllByBookId(bookId);
        return reviews.stream()
                .map(review -> new ResponseReview(
                        review.getReviewId(),
                        review.getEvaluationScore(),
                        review.getReviewContent(),
                        review.getReviewPhoto(),
                        review.getReviewedAt(),
                        review.getUpdatedAt(),
                        review.getUser().getUserId(),
                        review.getBookId()))
                .toList();
    }

}
