package com.nhnacademy.bookstoreuserapi.pointtype.service.impl;

import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointType;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointTypeUpdateRequest;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.pointtype.exception.PointTypeNotFoundException;
import com.nhnacademy.bookstoreuserapi.pointtype.repository.PointTypeRepository;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PointTypeServiceImpl implements PointTypeService {

    private final PointTypeRepository pointTypeRepository;
    private final UserGradeRepository userGradeRepository;

    @Override
    public void savePointType(PointTypeCreateRequest request) {

        PointType pointType = new PointType();
        pointType.setTypeName(request.typeName());
        pointType.setEarningPoint(request.earningPoint());
        pointType.setEarningRate(request.earningRate());
        pointType.setIsActive(request.isActive());

        UserGrade userGrade = userGradeRepository.findByGradeName(UserGrade.Grade.valueOf(request.gradeName()));

        pointType.setUserGrade(userGrade);

        pointTypeRepository.save(pointType);
    }

    @Override
    public Page<ResponsePointType> getAllPointTypes(Pageable pageable) {
        Page<PointType> pointTypes = pointTypeRepository.findAll(pageable);

        List<ResponsePointType> dtoList = pointTypes
                .stream()
                .map(pt -> new ResponsePointType(
                        pt.getTypeId(),
                        pt.getTypeName(),
                        pt.getEarningPoint(),
                        pt.getEarningRate(),
                        pt.getUserGrade().getGradeName().toString(),
                        pt.getIsActive()
                ))
                .toList();

        return new PageImpl<>(dtoList, pageable, pointTypes.getTotalElements());
    }

    @Override
    public Page<ResponsePointType> getPointTypeByGradeName(UserGrade.Grade gradeName, Pageable pageable) {
        return pointTypeRepository.findPointTypeByGradeName(gradeName, pageable);
    }

    @Override
    public void deletePointType(Long typeId) {

        if(!pointTypeRepository.existsById(typeId)) {
            throw new PointTypeNotFoundException(typeId);
        }

        pointTypeRepository.deleteById(typeId);
    }

    @Override
    public boolean isActivePointType(String typeName) {

        if(!pointTypeRepository.existsByTypeName(typeName)) {

            throw new PointTypeNotFoundException(pointTypeRepository.findTypeIdByTypeName(typeName));
        }

        return pointTypeRepository.isActiveByTypeName(typeName);
    }

    @Override
    public int getEarningPointByTypeName(String typeName) {

        if(pointTypeRepository.existsByTypeName(typeName)){
            return pointTypeRepository.findEarningPointByTypeName(typeName);
        }
        else throw new PointTypeNotFoundException(pointTypeRepository.findTypeIdByTypeName(typeName));
    }

    @Override
    public Long getTypeIdByName(String typeName) {

        if(pointTypeRepository.existsByTypeName(typeName)){
            return pointTypeRepository.findTypeIdByTypeName(typeName);
        }
        else throw new PointTypeNotFoundException(pointTypeRepository.findTypeIdByTypeName(typeName));
    }

    @Override
    public void updatePointTypeisActive(Long typeId) {

        PointType pointType = pointTypeRepository.findById(typeId).orElseThrow(() -> new PointTypeNotFoundException(typeId));

        pointType.setIsActive(!getPointTypeIsActive(typeId));
    }

    @Override
    public boolean getPointTypeIsActive(Long typeId) {

        if(!pointTypeRepository.existsById(typeId)) {
            throw new PointTypeNotFoundException(typeId);
        }

        return pointTypeRepository.findIsActiveByTypeId(typeId);
    }

    @Override
    public void updatePointTypeInfo(PointTypeUpdateRequest request, Long typeId) {

        PointType pointType = pointTypeRepository.findById(typeId).orElseThrow(() -> new PointTypeNotFoundException(typeId));

        pointType.setTypeName(request.typeName());
        pointType.setEarningPoint(request.earningPoint());
        pointType.setEarningRate(request.earningRate());
        pointType.setUserGrade(userGradeRepository.findByGradeName(UserGrade.Grade.valueOf(request.gradeName())));
    }

    @Override
    public ResponsePointType getPointTypeInfo(Long typeId) {

        PointType pointType = pointTypeRepository.findById(typeId).orElseThrow(() -> new PointTypeNotFoundException(typeId));

        ResponsePointType responsePointType = new ResponsePointType();

        responsePointType.setTypeId(typeId);
        responsePointType.setTypeName(pointType.getTypeName());
        responsePointType.setEarningPoint(pointType.getEarningPoint());
        responsePointType.setEarningRate(pointType.getEarningRate());
        responsePointType.setGradeName(pointType.getUserGrade().getGradeName().name());
        responsePointType.setActive(pointType.getIsActive());

        return responsePointType;
    }
}
