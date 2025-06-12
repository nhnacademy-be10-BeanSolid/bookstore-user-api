package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.Point;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.PointRepository;
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

    @Override
    @Transactional
    public ResponsePoint savePoint(PointCreateRequest pointCreateRequest) {

        if(userRepository.findById(pointCreateRequest.getUserId()).isEmpty()) {
            throw new UserNotFoundException(pointCreateRequest.getUserId());
        }

        Point point = new Point();
        User user = userRepository.findById(pointCreateRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(pointCreateRequest.getUserId()));
        point.setUser(user);
        point.setPaymentId(pointCreateRequest.getPaymentId());
        point.setTypeId(pointCreateRequest.getTypeId());
        point.setEarnedAndUsedAt(pointCreateRequest.getEarnedAndUsedAt());
        point.setEarnedAndUsedPoint(pointCreateRequest.getEarnedAndUsedPoint());

        Point savedPoint = pointRepository.save(point);

        return new ResponsePoint(
                savedPoint.getPointId(),
                savedPoint.getUser().getUserId(),
                savedPoint.getPaymentId(),
                savedPoint.getTypeId(),
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
                    point.getTypeId(),
                    point.getPaymentId(),
                    point.getEarnedAndUsedAt(),
                    point.getEarnedAndUsedPoint()
            ));
        }
        return responsePoints;
    }

}
