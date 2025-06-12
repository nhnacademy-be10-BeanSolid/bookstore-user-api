package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.PointType;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointTypeRepository extends JpaRepository<PointType, Long> {

    @Query("select p from PointType p where p.userGrade.gradeName = :gradeName")
    List<PointType> findPointTypeByGradeName(UserGrade.Grade gradeName);

    @Modifying(clearAutomatically = true)
    @Query("update PointType p set p.earningPoint = :point where p.typeId = :typeId")
    void updateEarningPoint(Long point, Long typeId);

    @Modifying(clearAutomatically = true)
    @Query("update PointType p set p.earningRate = :rate where p.typeId = :typeId")
    void updateEarningRate(int rate, Long typeId);
}
