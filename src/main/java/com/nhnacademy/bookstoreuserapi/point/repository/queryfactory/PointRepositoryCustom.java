package com.nhnacademy.bookstoreuserapi.point.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.point.domain.ResponsePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PointRepositoryCustom {
    Page<ResponsePoint> findPointByUserId(String userId, Pageable pageable);
}
