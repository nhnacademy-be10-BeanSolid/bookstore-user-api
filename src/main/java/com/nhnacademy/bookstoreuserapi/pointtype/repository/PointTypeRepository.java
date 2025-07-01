package com.nhnacademy.bookstoreuserapi.pointtype.repository;

import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointType;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.queryfactory.PointTypeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PointTypeRepository extends JpaRepository<PointType, Long>, PointTypeRepositoryCustom {

    @Query("select p.earningPoint from PointType p where p.typeName = :typeName")
    int findEarningPointByTypeName(String typeName);
}
