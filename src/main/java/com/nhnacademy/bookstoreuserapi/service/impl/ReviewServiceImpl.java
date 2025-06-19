package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import com.nhnacademy.bookstoreuserapi.exception.*;
import com.nhnacademy.bookstoreuserapi.repository.PointTypeRepository;
import com.nhnacademy.bookstoreuserapi.repository.ReviewRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.service.PointService;
import com.nhnacademy.bookstoreuserapi.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.nhnacademy.bookstoreuserapi.util.OwnerShipValidator.validate;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PointTypeRepository pointTypeRepository;
    private final PointService pointService;

    @Override
    public ResponseReview addReview(String userId, ReviewCreateRequest review){
        Review findReview = reviewRepository.findByUser_UserIdAndBookId(review.userId(), review.bookId());
        if (findReview != null) {
            throw new ReviewAlreadyExistsBookException(review.userId(), review.bookId());
        }
        validate(userId, review.userId());
        User user = userRepository.findById(review.userId())
                .orElseThrow(() -> new UserNotFoundException(review.userId()));
        Review savedReview = reviewRepository.save(new Review(review, user));

        int reviewPoint = pointTypeRepository.findEarningPointByTypeName("리뷰작성");

        userRepository.updatePointByUserId(review.userId(), reviewPoint);

        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                userId,
                2L,
                null,
                LocalDateTime.now(),
                reviewPoint
        );

        pointService.savePoint(userId,pointCreateRequest);

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
    public ResponseReview editReview(String userId, long reviewId, ReviewUpdateRequest review) {
        Review findReview = reviewRepository.findById(reviewId).orElse(null);
        if (findReview == null) {
            throw new ReviewNotFoundException(reviewId);
        }
        validate(userId, findReview.getUser().getUserId());
        findReview.setEvaluationScore(review.evaluationScore());
        findReview.setReviewContent(review.reviewContent());
        findReview.setReviewPhoto(review.reviewPhoto());
        findReview.setUpdatedAt(LocalDateTime.now());

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
    public Page<ResponseReview> getReviewsByUserId(String userId, Pageable pageable) {
        return reviewRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<ResponseReview> getReviewsByBookId(long bookId, Pageable pageable) {
        return reviewRepository.findAllByBookId(bookId, pageable);
    }

}
