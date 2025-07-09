package com.nhnacademy.bookstoreuserapi.pointtype.service;

import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointTypeService {

    void savePointType(PointTypeCreateRequest request);

    Page<ResponsePointType> getAllPointTypes(Pageable pageable);

    Page<ResponsePointType> getPointTypeByGradeName(UserGrade.Grade gradeName, Pageable pageable);

    void deletePointType(Long id);

    ResponsePointType updateEarningPoint(int point, Long typeId);

    ResponsePointType updateEarningRate(int rate, Long typeId);

    Boolean isActivePointType(String typeName);

    int getEarningPointByTypeName(String typeName);

    Long getTypeIdByName(String typeName);
}
