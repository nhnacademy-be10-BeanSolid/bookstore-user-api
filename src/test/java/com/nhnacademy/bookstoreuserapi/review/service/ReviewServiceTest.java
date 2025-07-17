package com.nhnacademy.bookstoreuserapi.review.service;

import com.nhnacademy.bookstoreuserapi.adapter.BookAdapter;
import com.nhnacademy.bookstoreuserapi.adapter.OrderAdapter;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PointService pointService;

    @Mock
    private PointTypeService pointTypeService;

    @Mock
    private MinioService minioService;

    @Mock
    private OrderAdapter orderAdapter;

    @Mock
    private BookAdapter bookAdapter;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private User user;
    private ReviewCreateRequest reviewCreateRequest;
    private Review review;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId("user123")
                .userPassword("password123")
                .userName("testUser")
                .userPhoneNumber("01012345678")
                .userEmail("test@example.com")
                .userBirth(LocalDate.now())
                .userPoint(1000)
                .isAuth(true)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .userGrade(new UserGrade(UserGrade.Grade.BASIC, 0L))
                .build();

        reviewCreateRequest = new ReviewCreateRequest(5, "Great book!", new ArrayList<>(), "user123", 1L);
        review = new Review(reviewCreateRequest, user);
        review.setReviewId(1L);
    }

    @Test
    @DisplayName("리뷰 추가 성공 - 이미지 없음")
    void addReview_NoImages_Success() {
        when(reviewRepository.findByUser_UserIdAndBookId("user123", 1L)).thenReturn(null);
        when(userRepository.findByUserId("user123")).thenReturn(user);
        when(orderAdapter.validatePurchase(user.getUserNo(), 1L)).thenReturn(true);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(pointTypeService.isActivePointType(anyString())).thenReturn(true);
        when(pointTypeService.getEarningPointByTypeName("리뷰")).thenReturn(100);
        when(pointTypeService.getTypeIdByName("리뷰")).thenReturn(1L);

        reviewService.addReview("user123", reviewCreateRequest);

        verify(reviewRepository).save(any(Review.class));
        verify(userRepository).updatePointByUserId("user123", 100);
        verify(pointService).savePoint(eq("user123"), any(PointCreateRequest.class));
    }

    @Test
    @DisplayName("리뷰 추가 성공 - 이미지 있음")
    void addReview_WithImages_Success() {
        reviewCreateRequest.imageUrls().add("http://example.com/image.jpg");
        when(reviewRepository.findByUser_UserIdAndBookId("user123", 1L)).thenReturn(null);
        when(userRepository.findByUserId("user123")).thenReturn(user);
        when(orderAdapter.validatePurchase(user.getUserNo(), 1L)).thenReturn(true);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(pointTypeService.isActivePointType(anyString())).thenReturn(true);
        when(pointTypeService.getEarningPointByTypeName("리뷰+사진")).thenReturn(200);
        when(pointTypeService.getTypeIdByName("리뷰+사진")).thenReturn(2L);

        reviewService.addReview("user123", reviewCreateRequest);

        verify(reviewRepository).save(any(Review.class));
        verify(userRepository).updatePointByUserId("user123", 200);
        verify(pointService).savePoint(eq("user123"), any(PointCreateRequest.class));
    }


    @Test
    @DisplayName("리뷰 추가 실패 - 이미 존재하는 리뷰")
    void addReview_AlreadyExists_Fail() {
        when(userRepository.findByUserId("user123")).thenReturn(user);
        when(orderAdapter.validatePurchase(user.getUserNo(), 1L)).thenReturn(true);
        when(reviewRepository.findByUser_UserIdAndBookId("user123", 1L)).thenReturn(review);

        assertThrows(ReviewAlreadyExistsBookException.class,
                () -> reviewService.addReview("user123", reviewCreateRequest));
    }

    @Test
    @DisplayName("리뷰 수정 성공 - 내용만 수정")
    void editReview_ContentOnly_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        ReviewUpdateRequest updateRequest = new ReviewUpdateRequest(4, "Updated content", new ArrayList<>());

        reviewService.editReview("user123", 1L, updateRequest);

        assertThat(review.getEvaluationScore()).isEqualTo(4);
        assertThat(review.getReviewContent()).isEqualTo("Updated content");
        assertThat(review.getUpdatedAt()).isNotNull();
        verify(minioService, never()).deleteImage(anyString());
    }

    @Test
    @DisplayName("리뷰 수정 성공 - 이미지 추가")
    void editReview_AddImage_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        List<String> newImageUrls = List.of("http://example.com/new.jpg");
        ReviewUpdateRequest updateRequest = new ReviewUpdateRequest(5, "Great book!", newImageUrls);

        reviewService.editReview("user123", 1L, updateRequest);

        assertThat(review.getReviewImages()).hasSize(1);
        assertThat(review.getReviewImages().getFirst().getImageUrl()).isEqualTo("http://example.com/new.jpg");
        verify(minioService, never()).deleteImage(anyString());
    }

    @Test
    @DisplayName("리뷰 수정 성공 - 이미지 삭제")
    void editReview_DeleteImage_Success() {
        review.getReviewImages().add(new ReviewImage(1L, "http://example.com/old.jpg", LocalDateTime.now(), review));
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        ReviewUpdateRequest updateRequest = new ReviewUpdateRequest(5, "Great book!", new ArrayList<>());

        reviewService.editReview("user123", 1L, updateRequest);

        assertThat(review.getReviewImages()).isEmpty();
        verify(minioService, times(1)).deleteImage("http://example.com/old.jpg");
    }

    @Test
    @DisplayName("리뷰 수정 성공 - 이미지 교체")
    void editReview_ReplaceImage_Success() {
        review.getReviewImages().add(new ReviewImage(1L, "http://example.com/old.jpg", LocalDateTime.now(), review));
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        List<String> newImageUrls = List.of("http://example.com/new.jpg");
        ReviewUpdateRequest updateRequest = new ReviewUpdateRequest(5, "Great book!", newImageUrls);

        reviewService.editReview("user123", 1L, updateRequest);

        assertThat(review.getReviewImages()).hasSize(1);
        assertThat(review.getReviewImages().getFirst().getImageUrl()).isEqualTo("http://example.com/new.jpg");
        verify(minioService, times(1)).deleteImage("http://example.com/old.jpg");
    }

    @Test
    @DisplayName("리뷰 수정 성공 - 사진 추가로 포인트 적립")
    void editReview_AddImage_PointGain() {
        // Arrange: 이미지가 없는 리뷰 준비
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(pointTypeService.isActivePointType("리뷰사진추가")).thenReturn(true);
        when(pointTypeService.getEarningPointByTypeName("리뷰사진추가")).thenReturn(50);
        when(pointTypeService.getTypeIdByName("리뷰사진추가")).thenReturn(3L);

        List<String> newImageUrls = List.of("http://example.com/new.jpg");
        ReviewUpdateRequest updateRequest = new ReviewUpdateRequest(5, "Great book!", newImageUrls);

        // Act
        reviewService.editReview("user123", 1L, updateRequest);

        // Assert
        assertThat(review.getReviewImages()).hasSize(1);
        verify(userRepository).updatePointByUserId("user123", 50);
        verify(pointService).savePoint(eq("user123"), any(PointCreateRequest.class));
    }

    @Test
    @DisplayName("리뷰 수정 성공 - 사진 삭제로 포인트 차감")
    void editReview_DeleteImage_PointLoss() {
        // Arrange: 이미지가 있는 리뷰 준비
        review.getReviewImages().add(new ReviewImage(1L, "http://example.com/old.jpg", LocalDateTime.now(), review));
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(pointTypeService.isActivePointType("리뷰사진삭제")).thenReturn(true);
        when(pointTypeService.getEarningPointByTypeName("리뷰사진삭제")).thenReturn(50); // 차감될 포인트 원금
        when(pointTypeService.getTypeIdByName("리뷰사진삭제")).thenReturn(4L);

        ReviewUpdateRequest updateRequest = new ReviewUpdateRequest(5, "Great book!", new ArrayList<>());

        // Act
        reviewService.editReview("user123", 1L, updateRequest);

        // Assert
        assertThat(review.getReviewImages()).isEmpty();
        verify(minioService).deleteImage("http://example.com/old.jpg");
        verify(userRepository).updatePointByUserId("user123", -50); // -50으로 차감되었는지 확인
        verify(pointService).savePoint(eq("user123"), any(PointCreateRequest.class));
    }


    @Test
    @DisplayName("리뷰 수정 실패 - 존재하지 않는 리뷰")
    void editReview_NotFound_Fail() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
        ReviewUpdateRequest request = new ReviewUpdateRequest(4, "Updated review", null);

        assertThrows(ReviewNotFoundException.class,
                () -> reviewService.editReview("user123", 1L, request));
    }

    @Test
    @DisplayName("리뷰 조회 성공")
    void getReview_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        ResponseReview result = reviewService.getReview(1L);

        assertThat(result.getReviewId()).isEqualTo(1L);
        verify(reviewRepository).findById(1L);
    }

    @Test
    @DisplayName("리뷰 조회 실패 - 존재하지 않는 리뷰")
    void getReview_NotFound_Fail() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReview(1L));
    }

    @Test
    @DisplayName("사용자 ID로 리뷰 목록 조회")
    void getReviewsByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        ResponseSimpleReviewByUser responseReview = new ResponseSimpleReviewByUser(1L, 5, "Great book!", LocalDateTime.now(), null, "user123", 1L);
        Page<ResponseSimpleReviewByUser> page = new PageImpl<>(Collections.singletonList(responseReview), pageable, 1);

        when(reviewRepository.findAllByUserId("user123", pageable)).thenReturn(page);

        Page<ResponseSimpleReviewByUser> result = reviewService.getReviewsByUserId("user123", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getUserId()).isEqualTo("user123");
        verify(reviewRepository).findAllByUserId("user123", pageable);
    }

    @Test
    @DisplayName("책 ID로 리뷰 목록 조회")
    void getReviewsByBookId() {
        long bookId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        ResponseSimpleReview responseReview = new ResponseSimpleReview(1L, 5, "Great book!", LocalDateTime.now(), null, "user123", 1L);
        Page<ResponseSimpleReview> page = new PageImpl<>(Collections.singletonList(responseReview), pageable, 1);

        when(reviewRepository.findAllByBookId(bookId, pageable)).thenReturn(page);

        Page<ResponseSimpleReview> result = reviewService.getReviewsByBookId(bookId, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getBookId()).isEqualTo(1L);
        verify(reviewRepository).findAllByBookId(bookId, pageable);
    }

    @Test
    @DisplayName("책 ID로 리뷰 개수 조회")
    void countReviewsByBookId() {
        long bookId = 1L;
        when(reviewRepository.countReviewsByBookId(bookId)).thenReturn(5L);

        long count = reviewService.countReviewsByBookId(bookId);

        assertThat(count).isEqualTo(5L);
        verify(reviewRepository).countReviewsByBookId(bookId);
    }

    @Test
    @DisplayName("책 ID로 평균 평가 점수 조회")
    void getAverageEvaluationScoreByBookId() {
        long bookId = 1L;
        when(reviewRepository.averageEvaluationScoreByBookId(bookId)).thenReturn(4.5);

        double averageScore = reviewService.getAverageEvaluationScoreByBookId(bookId);

        assertThat(averageScore).isEqualTo(4.5);
        verify(reviewRepository).averageEvaluationScoreByBookId(bookId);
    }
}
