package com.nhnacademy.bookstoreuserapi.pointtype.service;

import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointTypeUpdateRequest;
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

    boolean isActivePointType(String typeName);

    int getEarningPointByTypeName(String typeName);

    Long getTypeIdByName(String typeName);

    void updatePointTypeisActive(Long typeId);

    boolean getPointTypeIsActive(Long typeId);

    void updatePointTypeInfo(PointTypeUpdateRequest request, Long typeId);

    ResponsePointType getPointTypeInfo(Long typeId);
}
