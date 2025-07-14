package com.nhnacademy.bookstoreuserapi.review.service.impl;


import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.review.domain.Review;
import com.nhnacademy.bookstoreuserapi.review.exception.ReviewAlreadyExistsBookException;
import com.nhnacademy.bookstoreuserapi.review.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreuserapi.review.service.ReviewService;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.review.domain.ReviewUpdateRequest;
import com.nhnacademy.bookstoreuserapi.review.domain.ReviewCreateRequest;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseReview;
import com.nhnacademy.bookstoreuserapi.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.nhnacademy.bookstoreuserapi.common.exception.OwnerShipValidator.validate;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PointService pointService;
    private final PointTypeService pointTypeService;

    @Override
    public ResponseReview addReview(String userId, ReviewCreateRequest review){
        Review findReview = reviewRepository.findByUser_UserIdAndBookId(review.userId(), review.bookId());
        if (findReview != null) {
            throw new ReviewAlreadyExistsBookException(review.userId(), review.bookId());
        }
        validate(userId, review.userId());
        User user = userRepository.findByUserId(review.userId());
        if (user == null) {
            throw new UserNotFoundException(review.userId());
        }
        Review savedReview = reviewRepository.save(new Review(review, user));

        if(pointTypeService.isActivePointType("리뷰작성")){

            int reviewPoint = pointTypeService.getEarningPointByTypeName("리뷰작성");
            long typeId = pointTypeService.getTypeIdByName("리뷰작성");

            String reviewPointPlus = reviewPoint + "p 적립";

            userRepository.updatePointByUserId(review.userId(), reviewPoint);

            PointCreateRequest pointCreateRequest = new PointCreateRequest(
                    userId,
                    typeId,
                    null,
                    LocalDateTime.now(),
                    reviewPointPlus
            );

            pointService.savePoint(userId,pointCreateRequest);
        }

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
