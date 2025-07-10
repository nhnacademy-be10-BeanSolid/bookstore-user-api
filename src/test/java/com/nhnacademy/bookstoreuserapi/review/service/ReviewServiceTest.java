package com.nhnacademy.bookstoreuserapi.review.service;

import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.review.domain.*;
import com.nhnacademy.bookstoreuserapi.review.exception.ReviewAlreadyExistsBookException;
import com.nhnacademy.bookstoreuserapi.review.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreuserapi.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreuserapi.review.service.impl.ReviewServiceImpl;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    ReviewRepository reviewRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PointService pointService;

    @Mock
    PointTypeService pointTypeService;

    @InjectMocks
    ReviewServiceImpl reviewService;

    private User createUser(String userId) {
        UserGrade userGrade = new UserGrade(UserGrade.Grade.BASIC, 0L);
        return User.builder()
                .userId(userId)
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
    }

    @Test
    void addReview() {
        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(5, "Great book!", "", "user123", 1L);
        User user = createUser("user123");
        Review review = new Review(reviewCreateRequest, user);

        when(reviewRepository.findByUser_UserIdAndBookId("user123", 1L)).thenReturn(null);
        when(userRepository.findByUserId("user123")).thenReturn(user);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        when(pointTypeService.isActivePointType("리뷰작성")).thenReturn(true);
        when(pointTypeService.getEarningPointByTypeName("리뷰작성")).thenReturn(500);
        when(pointTypeService.getTypeIdByName("리뷰작성")).thenReturn(2L);

        reviewService.addReview("user123", reviewCreateRequest);

        verify(reviewRepository).findByUser_UserIdAndBookId("user123", 1L);
        verify(reviewRepository).save(any(Review.class));
        verify(pointTypeService).isActivePointType("리뷰작성");
        verify(pointTypeService).getEarningPointByTypeName("리뷰작성");
        verify(pointTypeService).getTypeIdByName("리뷰작성");
        verify(userRepository).updatePointByUserId("user123", 500);
        verify(pointService).savePoint(eq("user123"), any(PointCreateRequest.class));
    }

    @Test
    void addReviewFail() {
        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(5, "Great book!", "", "user123", 1L);
        User user = createUser("user123");
        Review review = new Review(reviewCreateRequest, user);

        when(reviewRepository.findByUser_UserIdAndBookId("user123", 1L)).thenReturn(review);

        Assertions.assertThrows(ReviewAlreadyExistsBookException.class,
                () -> reviewService.addReview("user123", reviewCreateRequest));

        verify(reviewRepository).findByUser_UserIdAndBookId("user123", 1L);
    }

    @Test
    void editReview() {
        long reviewId = 1L;
        User user = createUser("user123");
        Review existingReview = new Review(new ReviewCreateRequest(5, "Great book!", "", "user123", 1L), user);
        existingReview.setReviewId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        reviewService.editReview("user123", reviewId, new ReviewUpdateRequest(4, "Updated review", ""));

        verify(reviewRepository).findById(reviewId);
    }

    @Test
    void editReviewFail() {
        long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        ReviewUpdateRequest request = new ReviewUpdateRequest(4, "Updated review", "");
        Assertions.assertThrows(ReviewNotFoundException.class,
                () -> reviewService.editReview("user123", reviewId, request));

        verify(reviewRepository).findById(reviewId);
    }

    @Test
    void getReview() {
        long reviewId = 1L;
        User user = createUser("user123");
        Review existingReview = new Review(new ReviewCreateRequest(5, "Great book!", "", "user123", 1L), user);
        existingReview.setReviewId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        reviewService.getReview(reviewId);
        verify(reviewRepository).findById(reviewId);
    }

    @Test
    void getReviewFail() {
        long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ReviewNotFoundException.class, () -> reviewService.getReview(reviewId));

        verify(reviewRepository).findById(reviewId);
    }

    @Test
    void getReviewByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        ResponseReview responseReview = new ResponseReview(
                1L, 5, "Great book!", "", LocalDateTime.now(), LocalDateTime.now(), "user123", 1L
        );
        Page<ResponseReview> page = new PageImpl<>(Collections.singletonList(responseReview), pageable, 1);

        when(reviewRepository.findAllByUserId("user123", pageable)).thenReturn(page);

        Page<ResponseReview> result = reviewService.getReviewsByUserId("user123", pageable);

        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals("user123", result.getContent().getFirst().getUserId());
        verify(reviewRepository).findAllByUserId("user123", pageable);
    }

    @Test
    void getReviewByBookId() {
        long bookId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        ResponseReview responseReview = new ResponseReview(
                1L, 5, "Great book!", "", LocalDateTime.now(), LocalDateTime.now(), "user123", 1L
        );
        Page<ResponseReview> page = new PageImpl<>(Collections.singletonList(responseReview), pageable, 1);

        when(reviewRepository.findAllByBookId(bookId, pageable)).thenReturn(page);

        Page<ResponseReview> result = reviewService.getReviewsByBookId(bookId, pageable);

        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(1L, result.getContent().getFirst().getBookId());
        verify(reviewRepository).findAllByBookId(bookId, pageable);
    }
}
