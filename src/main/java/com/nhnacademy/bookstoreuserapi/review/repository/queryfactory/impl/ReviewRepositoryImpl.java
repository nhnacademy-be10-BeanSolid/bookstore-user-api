package com.nhnacademy.bookstoreuserapi.review.repository.queryfactory.impl;

import com.nhnacademy.bookstoreuserapi.review.domain.QReview;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseSimpleReview;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseSimpleReviewByUser;
import com.nhnacademy.bookstoreuserapi.review.repository.queryfactory.ReviewRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ResponseSimpleReviewByUser> findAllByUserId(String userId, Pageable pageable) {
        QReview review = QReview.review;
        List<ResponseSimpleReviewByUser> content = queryFactory
                .select(Projections.constructor(ResponseSimpleReviewByUser.class,
                        review.reviewId,
                        review.evaluationScore,
                        review.reviewContent,
                        review.reviewedAt,
                        review.updatedAt,
                        review.user.userId,
                        review.bookId))
                .from(review)
                .where(review.user.userId.eq(userId))
                .orderBy(review.reviewedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(review.user.userId.eq(userId))
                .fetchOne();
        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<ResponseSimpleReview> findAllByBookId(long bookId, Pageable pageable) {
        QReview review = QReview.review;
        List<ResponseSimpleReview> content = queryFactory
                .select(Projections.constructor(ResponseSimpleReview.class,
                        review.reviewId,
                        review.evaluationScore,
                        review.reviewContent,
                        review.reviewedAt,
                        review.updatedAt,
                        review.user.userId,
                        review.bookId))
                .from(review)
                .where(review.bookId.eq(bookId))
                .orderBy(review.reviewedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(review.bookId.eq(bookId))
                .fetchOne();
        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public double averageEvaluationScoreByBookId(long bookId) {
        QReview review = QReview.review;
        Double averageScore = queryFactory
                .select(review.evaluationScore.avg())
                .from(review)
                .where(review.bookId.eq(bookId))
                .fetchOne();
        return averageScore != null ? averageScore : 0.0;
    }
}
