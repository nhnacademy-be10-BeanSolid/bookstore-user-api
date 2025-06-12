package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;

import java.util.List;

public interface PointTypeService {

    void savePointType(PointTypeCreateRequest request);

    List<ResponsePointType> getAllPointTypes();

    List<ResponsePointType> getPointTypeByGradeName(UserGrade.Grade gradeName);

    void deletePointType(Long id);

    ResponsePointType updateEarningPoint(Long point, Long typeId);

    ResponsePointType updateEarningRate(int rate, Long typeId);
}
