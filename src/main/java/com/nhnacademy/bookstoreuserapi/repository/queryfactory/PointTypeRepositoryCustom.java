package com.nhnacademy.bookstoreuserapi.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointTypeRepositoryCustom {
    Page<ResponsePointType> findPointTypeByGradeName(UserGrade.Grade gradeName, Pageable pageable);
}
