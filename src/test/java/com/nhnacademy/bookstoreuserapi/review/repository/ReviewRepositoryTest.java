package com.nhnacademy.bookstoreuserapi.review.repository;

import com.nhnacademy.bookstoreuserapi.review.domain.QReview;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseReview;
import com.nhnacademy.bookstoreuserapi.review.repository.queryfactory.impl.ReviewRepositoryImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewRepositoryTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAQuery<ResponseReview> responseReviewQuery;

    @Mock
    private JPAQuery<Long> countQuery;

    private ReviewRepositoryImpl reviewRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewRepository = new ReviewRepositoryImpl(queryFactory);
    }

    @Test
    @DisplayName("userId 기준 리뷰 조회")
    void testFindAllByUserId() {
        String userId = "user123";
        Pageable pageable = PageRequest.of(0, 10);
        QReview review = QReview.review;

        ResponseReview mockReview = new ResponseReview(
                1L, 5, "Great!", "img.jpg", LocalDateTime.now(), LocalDateTime.now(), userId, 101L
        );

        when(queryFactory.select(Projections.constructor(ResponseReview.class,
                review.reviewId, review.evaluationScore, review.reviewContent,
                review.reviewPhoto, review.reviewedAt, review.updatedAt,
                review.user.userId, review.bookId))).thenReturn(responseReviewQuery);
        when(responseReviewQuery.from(review)).thenReturn(responseReviewQuery);
        when(responseReviewQuery.where(review.user.userId.eq(userId))).thenReturn(responseReviewQuery);
        when(responseReviewQuery.offset(pageable.getOffset())).thenReturn(responseReviewQuery);
        when(responseReviewQuery.limit(pageable.getPageSize())).thenReturn(responseReviewQuery);
        when(responseReviewQuery.fetch()).thenReturn(List.of(mockReview));

        when(queryFactory.select(review.count())).thenReturn(countQuery);
        when(countQuery.from(review)).thenReturn(countQuery);
        when(countQuery.where(review.user.userId.eq(userId))).thenReturn(countQuery);
        when(countQuery.fetchOne()).thenReturn(1L);

        Page<ResponseReview> result = reviewRepository.findAllByUserId(userId, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("bookId 기준 리뷰 조회")
    void testFindAllByBookId() {
        long bookId = 100L;
        Pageable pageable = PageRequest.of(0, 10);
        QReview review = QReview.review;

        ResponseReview mockReview = new ResponseReview(
                2L, 4, "Nice", null, LocalDateTime.now(), LocalDateTime.now(), "user456", bookId
        );

        when(queryFactory.select(Projections.constructor(ResponseReview.class,
                review.reviewId, review.evaluationScore, review.reviewContent,
                review.reviewPhoto, review.reviewedAt, review.updatedAt,
                review.user.userId, review.bookId))).thenReturn(responseReviewQuery);
        when(responseReviewQuery.from(review)).thenReturn(responseReviewQuery);
        when(responseReviewQuery.where(review.bookId.eq(bookId))).thenReturn(responseReviewQuery);
        when(responseReviewQuery.offset(pageable.getOffset())).thenReturn(responseReviewQuery);
        when(responseReviewQuery.limit(pageable.getPageSize())).thenReturn(responseReviewQuery);
        when(responseReviewQuery.fetch()).thenReturn(List.of(mockReview));

        when(queryFactory.select(review.count())).thenReturn(countQuery);
        when(countQuery.from(review)).thenReturn(countQuery);
        when(countQuery.where(review.bookId.eq(bookId))).thenReturn(countQuery);
        when(countQuery.fetchOne()).thenReturn(1L);

        Page<ResponseReview> result = reviewRepository.findAllByBookId(bookId, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getBookId()).isEqualTo(bookId);
    }
}