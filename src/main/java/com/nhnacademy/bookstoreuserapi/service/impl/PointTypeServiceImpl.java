package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.PointType;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.exception.PointTypeNotFoundException;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.PointTypeRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserGradeRepository;
import com.nhnacademy.bookstoreuserapi.service.PointTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointTypeServiceImpl implements PointTypeService {

    private final PointTypeRepository pointTypeRepository;
    private final UserGradeRepository userGradeRepository;

    @Override
    @Transactional
    public void savePointType(PointTypeCreateRequest request) {

        PointType pointType = new PointType();
        pointType.setTypeName(request.getTypeName());
        pointType.setEarningPoint(request.getEarningPoint());
        pointType.setEarningRate(request.getEarningRate());

        UserGrade userGrade = userGradeRepository.findByGradeName(UserGrade.Grade.valueOf(request.getGradeName()));

        if(userGrade == null) {
            throw new UserGradeNotFoundException(request.getGradeName());
        }

        pointType.setUserGrade(userGrade);

        pointTypeRepository.save(pointType);
    }

    @Override
    public List<ResponsePointType> getAllPointTypes() {

        List<PointType> pointTypes = pointTypeRepository.findAll();

        List<ResponsePointType> responsePointTypes = new ArrayList<>();

        for(PointType pointType : pointTypes) {
            responsePointTypes.add(new ResponsePointType(
                    pointType.getTypeId(),
                    pointType.getTypeName(),
                    pointType.getEarningPoint(),
                    pointType.getEarningRate(),
                    pointType.getUserGrade().getGradeName().toString()
            ));
        }
        return responsePointTypes;
    }

    @Override
    public List<ResponsePointType> getPointTypeByGradeName(UserGrade.Grade GradeName) {

        List<PointType> pointTypes = pointTypeRepository.findPointTypeByGradeName(GradeName);

        List<ResponsePointType> responsePointTypes = new ArrayList<>();

        for(PointType pointType : pointTypes) {
            responsePointTypes.add(new ResponsePointType(
                    pointType.getTypeId(),
                    pointType.getTypeName(),
                    pointType.getEarningPoint(),
                    pointType.getEarningRate(),
                    pointType.getUserGrade().getGradeName().toString()
                    ));
        }
        return responsePointTypes;
    }

    @Override
    @Transactional
    public void deletePointType(Long typeId) {

        if(!pointTypeRepository.existsById(typeId)) {
            throw new PointTypeNotFoundException(typeId);
        }

        pointTypeRepository.deleteById(typeId);
    }

    @Override
    @Transactional
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
    @Transactional
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
