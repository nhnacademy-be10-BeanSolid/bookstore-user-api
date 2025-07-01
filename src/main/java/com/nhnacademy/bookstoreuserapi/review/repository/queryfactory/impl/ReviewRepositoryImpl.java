package com.nhnacademy.bookstoreuserapi.review.repository.queryfactory.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.QReview;
import com.nhnacademy.bookstoreuserapi.review.domain.ResponseReview;
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
    public Page<ResponseReview> findAllByUserId(String userId, Pageable pageable) {
        QReview review = QReview.review;
        List<ResponseReview> content = queryFactory
                .select(Projections.constructor(ResponseReview.class,
                        review.reviewId,
                        review.evaluationScore,
                        review.reviewContent,
                        review.reviewPhoto,
                        review.reviewedAt,
                        review.updatedAt,
                        review.user.userId,
                        review.bookId))
                .from(review)
                .where(review.user.userId.eq(userId))
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
    public Page<ResponseReview> findAllByBookId(long bookId, Pageable pageable) {
        QReview review = QReview.review;
        List<ResponseReview> content = queryFactory
                .select(Projections.constructor(ResponseReview.class,
                        review.reviewId,
                        review.evaluationScore,
                        review.reviewContent,
                        review.reviewPhoto,
                        review.reviewedAt,
                        review.updatedAt,
                        review.user.userId,
                        review.bookId))
                .from(review)
                .where(review.bookId.eq(bookId))
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
}
