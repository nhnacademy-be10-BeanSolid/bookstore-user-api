package com.nhnacademy.bookstoreuserapi.service;


import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import com.nhnacademy.bookstoreuserapi.exception.ReviewAlreadyExistsBookException;
import com.nhnacademy.bookstoreuserapi.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.PointTypeRepository;
import com.nhnacademy.bookstoreuserapi.repository.ReviewRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    ReviewRepository reviewRepository;
    
    @Mock
    UserRepository userRepository;

    @Mock
    PointTypeRepository pointTypeRepository;

    @Mock
    PointService pointService;

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
                .userGrade(userGrade)
                .build();
        Review review = new Review(reviewCreateRequest, user);
        Mockito.when(userRepository.findByUserId(reviewCreateRequest.userId())).thenReturn(user);
        Mockito.when(reviewRepository.findByUser_UserIdAndBookId(review.getUser().getUserId(), review.getBookId())).thenReturn(null);
        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);
        Mockito.when(pointTypeRepository.findEarningPointByTypeName("리뷰작성")).thenReturn(500);



        reviewService.addReview("user123", reviewCreateRequest);
        Mockito.verify(reviewRepository, Mockito.times(1)).findByUser_UserIdAndBookId(review.getUser().getUserId(), review.getBookId());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any(Review.class));
        Mockito.verify(pointTypeRepository).findEarningPointByTypeName("리뷰작성");
        Mockito.verify(userRepository).updatePointByUserId("user123", 500);

        Mockito.verify(pointService).savePoint(
                Mockito.eq("user123"),
                Mockito.argThat(request ->
                        request.userId().equals("user123") &&
                                request.typeId().equals(2L) &&
                                request.paymentId() == null &&
                                request.earnedAndUsedPoint() == 500
                )
        );
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
                .userGrade(userGrade)
                .build();
        Review review = new Review(reviewCreateRequest, user);
        Mockito.when(reviewRepository.findByUser_UserIdAndBookId(review.getUser().getUserId(), review.getBookId())).thenReturn(review);

        Assertions.assertThrows(ReviewAlreadyExistsBookException.class, () -> reviewService.addReview("user123", reviewCreateRequest));

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
                .userGrade(userGrade)
                .build();
        Review existingReview = new Review(new ReviewCreateRequest(5, "Great book!", "", "user123", 1L), user);
        existingReview.setReviewId(reviewId);

        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        reviewService.editReview("user123", reviewId, new ReviewUpdateRequest(4, "Updated review", ""));

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    void editReviewFail() {
        long reviewId = 1L;
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        ReviewUpdateRequest request = new ReviewUpdateRequest(4, "Updated review", "");
        Assertions.assertThrows(ReviewNotFoundException.class,
                () -> reviewService.editReview("user123", reviewId, request));

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
        Pageable pageable = PageRequest.of(0, 10);
        ResponseReview responseReview = new ResponseReview(
                1L,
                5,
                "Great book!",
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "user123",
                1L
        );
        Page<ResponseReview> page = new PageImpl<>(Collections.singletonList(responseReview), pageable, 1);

        Mockito.when(reviewRepository.findAllByUserId("user123", pageable)).thenReturn(page);

        Page<ResponseReview> result = reviewService.getReviewsByUserId("user123", pageable);

        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals("user123", result.getContent().getFirst().getUserId());
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByUserId("user123", pageable);
    }



    @Test
    void getReviewByBookId() {
        long bookId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        ResponseReview responseReview = new ResponseReview(
                1L,
                5,
                "Great book!",
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "user123",
                1L
        );
        Page<ResponseReview> page = new PageImpl<>(Collections.singletonList(responseReview), pageable, 1);

        Mockito.when(reviewRepository.findAllByBookId(bookId, pageable)).thenReturn(page);

        Page<ResponseReview> result = reviewService.getReviewsByBookId(bookId, pageable);

        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(1L, result.getContent().getFirst().getBookId()); // getter에 따라 수정
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByBookId(bookId, pageable);

    }

}
