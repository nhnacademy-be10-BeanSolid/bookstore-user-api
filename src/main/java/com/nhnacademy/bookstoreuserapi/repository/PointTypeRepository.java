package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.PointType;
import com.nhnacademy.bookstoreuserapi.repository.queryfactory.PointTypeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PointTypeRepository extends JpaRepository<PointType, Long>, PointTypeRepositoryCustom {

    @Query("select p.earningPoint from PointType p where p.typeName = :typeName")
    int findEarningPointByTypeName(String typeName);
}
