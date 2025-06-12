package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestReview;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestReview;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import com.nhnacademy.bookstoreuserapi.exception.*;
import com.nhnacademy.bookstoreuserapi.repository.ReviewRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ResponseReview addReview(SignUpRequestReview review){
        if(review == null
                || review.getUserId() == null
                || review.getBookId() <= 0L) {
            throw new InvalidReviewDataException("Invalid review data");  //InvalidReviewDataException말고 그냥 InvalidDataException으로 해도 될 것 같음 나중에 합친 후에 수정
        }
        Review findReview = reviewRepository.findByUser_UserIdAndBookId(review.getUserId(), review.getBookId());
        if (findReview != null) {
            throw new ReviewAlreadyExistsBookException(review.getUserId(), review.getBookId());
        }
        User user = userRepository.findById(review.getUserId())
                .orElseThrow(() -> new UserNotFoundException(review.getUserId()));
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

    public ResponseReview editReview(long reviewId, EditRequestReview review) {
        Review findReview = reviewRepository.findById(reviewId).orElse(null);
        if (findReview == null) {
            throw new ReviewNotFoundException(reviewId);
        }
        findReview.setEvaluationScore(review.getEvaluationScore());
        findReview.setReviewContent(review.getReviewContent());
        findReview.setReviewPhoto(review.getReviewPhoto());
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

    public List<ResponseReview> getReviewsByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
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

    public List<ResponseReview> getReviewsByBookId(long bookId) {
        if (bookId <= 0) {
            throw new InvalidDataException("Invalid book ID");
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
                        review.getUser().getUserId(),
                        review.getBookId()))
                .toList();
    }

}
