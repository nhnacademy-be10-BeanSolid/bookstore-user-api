package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGradeRepository extends JpaRepository<UserGrade, UserGrade.Grade> {
    boolean existsByGradeName(UserGrade.Grade gradeName);

    UserGrade findByGradeName(UserGrade.Grade grade);

    UserGrade findTopByRequiredMoneyLessThanEqualOrderByRequiredMoneyDesc(long money);
}
