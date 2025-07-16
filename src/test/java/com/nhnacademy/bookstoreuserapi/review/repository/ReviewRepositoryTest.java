package com.nhnacademy.bookstoreuserapi.review.repository;

import com.nhnacademy.bookstoreuserapi.review.domain.QReview;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseSimpleReview;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseSimpleReviewByUser;
import com.nhnacademy.bookstoreuserapi.review.repository.queryfactory.impl.ReviewRepositoryImpl;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReviewRepositoryTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Mock
    private JPAQuery<ResponseSimpleReviewByUser> responseReviewByUserQuery;

    @Mock
    private JPAQuery<Double> responseReviewQuery;

    @Mock
    private JPAQuery<ResponseSimpleReview> responseReviewByBookIdQuery;

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

        ResponseSimpleReviewByUser mockReview = new ResponseSimpleReviewByUser(
                1L, 5, "Great!", LocalDateTime.now(), LocalDateTime.now(), userId, 101L
        );

        when(queryFactory.select(Projections.constructor(ResponseSimpleReviewByUser.class,
                review.reviewId, review.evaluationScore, review.reviewContent,
                review.reviewedAt, review.updatedAt,
                review.user.userId, review.bookId))).thenReturn(responseReviewByUserQuery);
        when(responseReviewByUserQuery.from(review)).thenReturn(responseReviewByUserQuery);
        when(responseReviewByUserQuery.where(review.user.userId.eq(userId))).thenReturn(responseReviewByUserQuery);
        when(responseReviewByUserQuery.orderBy(any(OrderSpecifier.class))).thenReturn(responseReviewByUserQuery);
        when(responseReviewByUserQuery.offset(pageable.getOffset())).thenReturn(responseReviewByUserQuery);
        when(responseReviewByUserQuery.limit(pageable.getPageSize())).thenReturn(responseReviewByUserQuery);
        when(responseReviewByUserQuery.fetch()).thenReturn(List.of(mockReview));

        when(queryFactory.select(review.count())).thenReturn(countQuery);
        when(countQuery.from(review)).thenReturn(countQuery);
        when(countQuery.where(review.user.userId.eq(userId))).thenReturn(countQuery);
        when(countQuery.fetchOne()).thenReturn(1L);

        Page<ResponseSimpleReviewByUser> result = reviewRepository.findAllByUserId(userId, pageable);

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

        ResponseSimpleReview mockReview = new ResponseSimpleReview(
                2L, 4, "Nice", LocalDateTime.now(), LocalDateTime.now(), "user456", bookId
        );

        when(queryFactory.select(Projections.constructor(ResponseSimpleReview.class,
                review.reviewId, review.evaluationScore, review.reviewContent,
                review.reviewedAt, review.updatedAt,
                review.user.userId, review.bookId))).thenReturn(responseReviewByBookIdQuery);
        when(responseReviewByBookIdQuery.from(review)).thenReturn(responseReviewByBookIdQuery);
        when(responseReviewByBookIdQuery.where(review.bookId.eq(bookId))).thenReturn(responseReviewByBookIdQuery);
        when(responseReviewByBookIdQuery.orderBy(any(OrderSpecifier.class))).thenReturn(responseReviewByBookIdQuery);
        when(responseReviewByBookIdQuery.offset(pageable.getOffset())).thenReturn(responseReviewByBookIdQuery);
        when(responseReviewByBookIdQuery.limit(pageable.getPageSize())).thenReturn(responseReviewByBookIdQuery);
        when(responseReviewByBookIdQuery.fetch()).thenReturn(List.of(mockReview));

        when(queryFactory.select(review.count())).thenReturn(countQuery);
        when(countQuery.from(review)).thenReturn(countQuery);
        when(countQuery.where(review.bookId.eq(bookId))).thenReturn(countQuery);
        when(countQuery.fetchOne()).thenReturn(1L);

        Page<ResponseSimpleReview> result = reviewRepository.findAllByBookId(bookId, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getBookId()).isEqualTo(bookId);
    }

    @Test
    @DisplayName("평균 평점 조회")
    void testAverageEvaluationScoreByBookId() {
        long bookId = 100L;
        QReview review = QReview.review;

        when(queryFactory.select(review.evaluationScore.avg())).thenReturn(responseReviewQuery);
        when(responseReviewQuery.from(review)).thenReturn(responseReviewQuery);
        when(responseReviewQuery.where(review.bookId.eq(bookId))).thenReturn(responseReviewQuery);
        when(responseReviewQuery.fetchOne()).thenReturn(4.5);

        double averageScore = reviewRepository.averageEvaluationScoreByBookId(bookId);

        assertThat(averageScore).isEqualTo(4.5);
    }
}