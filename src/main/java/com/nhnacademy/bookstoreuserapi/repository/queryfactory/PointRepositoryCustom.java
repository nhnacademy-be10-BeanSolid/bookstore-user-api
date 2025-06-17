package com.nhnacademy.bookstoreuserapi.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PointRepositoryCustom {
    Page<ResponsePoint> findPointByUserId(String userId, Pageable pageable);
}
