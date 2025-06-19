package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.PointType;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.exception.PointTypeNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.PointTypeRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserGradeRepository;
import com.nhnacademy.bookstoreuserapi.service.PointTypeService;
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
                        pt.getUserGrade().getGradeName().toString()
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
    public ResponsePointType updateEarningPoint(Long point, Long typeId) {

        pointTypeRepository.updateEarningPoint(point, typeId);

        PointType pointType = pointTypeRepository.findById(typeId)
                .orElseThrow(() -> new PointTypeNotFoundException(typeId));

        return new ResponsePointType(
                pointType.getTypeId(),
                pointType.getTypeName(),
                pointType.getEarningPoint(),
                pointType.getEarningRate(),
                pointType.getUserGrade().getGradeName().toString()

        );
    }

    @Override
    public ResponsePointType updateEarningRate(int rate, Long typeId) {

        pointTypeRepository.updateEarningRate(rate, typeId);

        PointType pointType = pointTypeRepository.findById(typeId)
                .orElseThrow(() -> new PointTypeNotFoundException(typeId));

        return new ResponsePointType(
                pointType.getTypeId(),
                pointType.getTypeName(),
                pointType.getEarningPoint(),
                pointType.getEarningRate(),
                pointType.getUserGrade().getGradeName().toString()
        );
    }
}
