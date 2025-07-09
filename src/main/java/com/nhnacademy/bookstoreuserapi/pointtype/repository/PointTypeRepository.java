package com.nhnacademy.bookstoreuserapi.pointtype.repository;

import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointType;
import com.nhnacademy.bookstoreuserapi.pointtype.repository.queryfactory.PointTypeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PointTypeRepository extends JpaRepository<PointType, Long>, PointTypeRepositoryCustom {

    @Query("select p.earningPoint from PointType p where p.typeName = :typeName")
    int findEarningPointByTypeName(String typeName);

    @Query("select p.typeId from PointType p where p.typeName = :typeName")
    Long findTypeIdByTypeName(String typeName);

    @Query("select p.isActive from PointType p where p.typeName = :typeName")
    boolean findIsActiveByTypeName(String typeName);

    boolean existsByTypeName(String typeName);
}
