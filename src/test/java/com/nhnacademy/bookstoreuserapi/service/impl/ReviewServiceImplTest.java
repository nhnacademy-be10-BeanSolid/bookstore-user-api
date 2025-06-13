package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewCreateRequest;
import com.nhnacademy.bookstoreuserapi.exception.ReviewAlreadyExistsBookException;
import com.nhnacademy.bookstoreuserapi.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.ReviewRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
    @Mock
    ReviewRepository reviewRepository;


    @Mock
    UserRepository userRepository;

    @InjectMocks
    ReviewServiceImpl reviewService;

    @Test
    void addReview() {
        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(5, "Great book!", "", "user123", 1L);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .orderMoney(0)
                .userGrade(userGrade)
                .build();
        Review review = new Review(reviewCreateRequest, user);
        Mockito.when(userRepository.findById(reviewCreateRequest.userId())).thenReturn(Optional.of(user));
        Mockito.when(reviewRepository.findByUser_UserIdAndBookId(review.getUser().getUserId(), review.getBookId())).thenReturn(null);
        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        reviewService.addReview(reviewCreateRequest);
        Mockito.verify(reviewRepository, Mockito.times(1)).findByUser_UserIdAndBookId(review.getUser().getUserId(), review.getBookId());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any(Review.class));
    }

    @Test
    void addReviewFail() {
        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(5, "Great book!", "", "user123", 1L);
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .orderMoney(0)
                .userGrade(userGrade)
                .build();
        Review review = new Review(reviewCreateRequest, user);
        Mockito.when(reviewRepository.findByUser_UserIdAndBookId(review.getUser().getUserId(), review.getBookId())).thenReturn(review);

        Assertions.assertThrows(ReviewAlreadyExistsBookException.class, () -> reviewService.addReview(reviewCreateRequest));

        Mockito.verify(reviewRepository, Mockito.times(1)).findByUser_UserIdAndBookId(review.getUser().getUserId(), review.getBookId());
    }

    @Test
    void editReview() {
        long reviewId = 1L;
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .orderMoney(0)
                .userGrade(userGrade)
                .build();
        Review existingReview = new Review(new ReviewCreateRequest(5, "Great book!", "", "user123", 1L), user);
        existingReview.setReviewId(reviewId);

        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(existingReview);

        reviewService.editReview(reviewId, new ReviewUpdateRequest(4, "Updated review", ""));

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    void editReviewFail() {
        long reviewId = 1L;
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        ReviewUpdateRequest request = new ReviewUpdateRequest(4, "Updated review", "");
        Assertions.assertThrows(ReviewNotFoundException.class,
                () -> reviewService.editReview(reviewId, request));

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    void getReview() {
        long reviewId = 1L;
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .orderMoney(0)
                .userGrade(userGrade)
                .build();
        Review existingReview = new Review(new ReviewCreateRequest(5, "Great book!", "", "user123", 1L), user);
        existingReview.setReviewId(reviewId);

        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        reviewService.getReview(reviewId);
        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    void getReviewFail() {
        long reviewId = 1L;
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ReviewNotFoundException.class, () -> reviewService.getReview(reviewId));

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    void getReviewByUserId() {
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .orderMoney(0)
                .userGrade(userGrade)
                .build();
        Review review = new Review(5, "Great book!", "", user, 1L);
        Mockito.when(reviewRepository.findAllByUser_UserId("user123")).thenReturn(Collections.singletonList(review));
        reviewService.getReviewsByUserId("user123");
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByUser_UserId("user123");
    }


    @Test
    void getReviewByBookId() {
        long bookId = 1L;
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        User user = User.builder()
                .userId("user123")
                .userPassword("pass")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .orderMoney(0)
                .userGrade(userGrade)
                .build();
        Review review = new Review(5, "Great book!", "", user, bookId);
        Mockito.when(reviewRepository.findAllByBookId(bookId)).thenReturn(Collections.singletonList(review));
        reviewService.getReviewsByBookId(bookId);
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByBookId(bookId);
    }

}
