package com.nhnacademy.bookstoreuserapi.point.repository;

import com.nhnacademy.bookstoreuserapi.point.domain.Point;
import com.nhnacademy.bookstoreuserapi.point.repository.queryfactory.PointRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {


}
