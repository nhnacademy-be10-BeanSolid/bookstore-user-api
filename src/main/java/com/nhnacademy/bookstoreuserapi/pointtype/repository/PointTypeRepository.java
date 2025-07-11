package com.nhnacademy.bookstoreuserapi.pointtype.repository;

import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointType;
import com.nhnacademy.bookstoreuserapi.pointtype.repository.queryfactory.PointTypeRepositoryCustom;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PointTypeRepository extends JpaRepository<PointType, Long>, PointTypeRepositoryCustom {

    @Query("select p.earningPoint from PointType p where p.typeName = :typeName")
    int findEarningPointByTypeName(String typeName);

    @Query("select p.typeId from PointType p where p.typeName = :typeName")
    Long findTypeIdByTypeName(String typeName);

    @Query("select p.isActive from PointType p where p.typeName = :typeName")
    boolean isActiveByTypeName(String typeName);

    boolean existsByTypeName(String typeName);

    @Query("select p.isActive from PointType p where p.typeId = :typeId")
    boolean findIsActiveByTypeId(Long typeId);

    @Query("select p from PointType p where p.earningRate != 0 and p.userGrade.gradeName = :gradeName")
    PointType findEarningRateByGradeName(UserGrade.Grade gradeName);
}
