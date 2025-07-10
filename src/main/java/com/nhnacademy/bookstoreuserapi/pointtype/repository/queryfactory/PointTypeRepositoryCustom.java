package com.nhnacademy.bookstoreuserapi.pointtype.repository.queryfactory;

import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface PointTypeRepositoryCustom {

    Page<ResponsePointType> findPointTypeByGradeName(UserGrade.Grade gradeName, Pageable pageable);
}
