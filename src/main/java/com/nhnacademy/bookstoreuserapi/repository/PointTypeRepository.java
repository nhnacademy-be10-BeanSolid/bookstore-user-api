package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.PointType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointTypeRepository extends JpaRepository<PointType, Long> {

    @Query("select p from PointType p where p.gradeName = :gradeName")
    List<PointType> findPointTypeByGradeName(String gradeName);

    @Modifying(clearAutomatically = true)
    @Query("update PointType p set p.earningPoint = :point where p.gradeName = :gradeName and p.typeName = :typeName")
    PointType updateEarningPoint(Long point, String gradeName, String typeName);

    @Modifying(clearAutomatically = true)
    @Query("update PointType p set p.earningRate = :rate where p.gradeName = :gradeName and p.typeName = :typeName")
    PointType updateEarningRate(int rate, String gradeName, String typeName);
}
