package com.nhnacademy.bookstoreuserapi.user.repository.impl;

import com.nhnacademy.bookstoreuserapi.user.domain.QUser;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepositoryCustom;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public long bulkUpdateUserGrade(UserGrade grade, List<Long> userNos) {
        QUser user = QUser.user;

        return queryFactory
                .update(user)
                .set(user.userGrade, grade)
                .where(user.userNo.in(userNos)
                        .and(user.userGrade.ne(grade)))
                .execute();
    }

    @Override
    public List<Long> findUserNosWithDifferentGrade(List<Long> userNos, UserGrade.Grade gradeName) {
        QUser user = QUser.user;
        return queryFactory
                .select(user.userNo)
                .from(user)
                .where(user.userNo.in(userNos)
                        .and(user.userGrade.gradeName.ne(gradeName)))
                .fetch();
    }
}