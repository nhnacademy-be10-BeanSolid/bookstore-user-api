package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointTypeService {

    void savePointType(PointTypeCreateRequest request);

    Page<ResponsePointType> getAllPointTypes(Pageable pageable);

    Page<ResponsePointType> getPointTypeByGradeName(UserGrade.Grade gradeName, Pageable pageable);

    void deletePointType(Long id);

    ResponsePointType updateEarningPoint(int point, Long typeId);

    ResponsePointType updateEarningRate(int rate, Long typeId);
}
