package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.Point;
import com.nhnacademy.bookstoreuserapi.repository.queryfactory.PointRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {

}
