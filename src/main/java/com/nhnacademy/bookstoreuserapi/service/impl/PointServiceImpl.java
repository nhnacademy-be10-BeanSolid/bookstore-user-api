package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.Point;
import com.nhnacademy.bookstoreuserapi.domain.entity.PointType;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.exception.PointTypeNotFoundException;
import com.nhnacademy.bookstoreuserapi.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.PointRepository;
import com.nhnacademy.bookstoreuserapi.repository.PointTypeRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.service.PointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final PointTypeRepository pointTypeRepository;

    @Override
    @Transactional
    public ResponsePoint savePoint(PointCreateRequest pointCreateRequest) {


        User user = userRepository.findById(pointCreateRequest.userId())
                .orElseThrow(() -> new UserNotFoundException(pointCreateRequest.userId()));
        Point point = new Point();
        point.setUser(user);
        point.setPaymentId(pointCreateRequest.paymentId());
        point.setTypeId(pointCreateRequest.typeId());
        point.setEarnedAndUsedAt(pointCreateRequest.earnedAndUsedAt());
        point.setEarnedAndUsedPoint(pointCreateRequest.earnedAndUsedPoint());
        point.setPaymentId(pointCreateRequest.paymentId());

        PointType pointType = pointTypeRepository.findById(pointCreateRequest.typeId())
                .orElseThrow(() -> new PointTypeNotFoundException(pointCreateRequest.typeId()));

        point.setPointType(pointType);

        point.setEarnedAndUsedAt(pointCreateRequest.earnedAndUsedAt());
        point.setEarnedAndUsedPoint(pointCreateRequest.earnedAndUsedPoint());

        Point savedPoint = pointRepository.save(point);

        return new ResponsePoint(
                savedPoint.getPointId(),
                savedPoint.getUser().getUserId(),
                savedPoint.getPaymentId(),
                savedPoint.getPointType().getTypeId(),
                savedPoint.getEarnedAndUsedAt(),
                savedPoint.getEarnedAndUsedPoint()
        );
    }

    @Override
    public List<ResponsePoint> findAll(String userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        List<Point> points = pointRepository.findPointByUserId(userId);
        List<ResponsePoint> responsePoints = new ArrayList<>();
        for (Point point : points) {
            responsePoints.add(new ResponsePoint(
                    point.getPointId(),
                    point.getUser().getUserId(),
                    point.getPointType().getTypeId(),
                    point.getPaymentId(),
                    point.getEarnedAndUsedAt(),
                    point.getEarnedAndUsedPoint()
            ));
        }
        return responsePoints;
    }

}
