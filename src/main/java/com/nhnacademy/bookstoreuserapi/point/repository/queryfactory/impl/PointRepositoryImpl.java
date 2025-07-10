package com.nhnacademy.bookstoreuserapi.point.repository.queryfactory.impl;

import com.nhnacademy.bookstoreuserapi.point.domain.QPoint;
import com.nhnacademy.bookstoreuserapi.point.domain.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.point.repository.queryfactory.PointRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ResponsePoint> findPointByUserId(String userId, Pageable pageable){
        QPoint point = QPoint.point;
        List<ResponsePoint> content = queryFactory
                .select(Projections.constructor(ResponsePoint.class,
                        point.pointId,
                        point.user.userId,
                        point.pointType.typeId,
                        point.orderNo,
                        point.earnedAndUsedAt,
                        point.earnedAndUsedPoint))
                .from(point)
                .where(point.user.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(point.count())
                .from(point)
                .where(point.user.userId.eq(userId))
                .fetchOne();
        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

}
