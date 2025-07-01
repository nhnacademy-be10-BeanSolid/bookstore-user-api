package com.nhnacademy.bookstoreuserapi.usergrade.repository;

import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGradeRepository extends JpaRepository<UserGrade, UserGrade.Grade> {

    boolean existsByGradeName(UserGrade.Grade gradeName);

    UserGrade findByGradeName(UserGrade.Grade grade);


}
