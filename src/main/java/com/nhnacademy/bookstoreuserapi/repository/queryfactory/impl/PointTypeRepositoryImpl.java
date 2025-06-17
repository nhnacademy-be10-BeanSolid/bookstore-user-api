package com.nhnacademy.bookstoreuserapi.repository.queryfactory.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.QPointType;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.repository.queryfactory.PointTypeRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
@RequiredArgsConstructor
public class PointTypeRepositoryImpl implements PointTypeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ResponsePointType> findPointTypeByGradeName(UserGrade.Grade gradeName, Pageable pageable) {
        QPointType qPointType = QPointType.pointType;
        List<ResponsePointType> content = queryFactory
                .select(Projections.constructor(ResponsePointType.class,
                        qPointType.typeId,
                        qPointType.typeName,
                        qPointType.earningPoint,
                        qPointType.earningRate,
                        qPointType.userGrade.gradeName.stringValue()))
                .from(qPointType)
                .where(qPointType.userGrade.gradeName.eq(gradeName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(qPointType.count())
                .from(qPointType)
                .where(qPointType.userGrade.gradeName.eq(gradeName))
                .fetchOne();
        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
