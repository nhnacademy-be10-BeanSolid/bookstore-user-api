package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;

import java.util.List;

public interface PointTypeService {

    void savePointType(PointTypeCreateRequest request);

    List<ResponsePointType> getAllPointTypes();

    List<ResponsePointType> getPointTypeByGradeName(String GradeName);

    void deletePointType(Long id);

    ResponsePointType updateEarningPoint(Long point, String gradeName, String typeName);

    ResponsePointType updateEarningRate(int rate, String gradeName, String typeName);
}
