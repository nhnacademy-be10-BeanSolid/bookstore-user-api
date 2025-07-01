package com.nhnacademy.bookstoreuserapi.point.service.impl;

import com.nhnacademy.bookstoreuserapi.point.domain.Point;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointType;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.pointtype.exception.PointTypeNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.point.repository.PointRepository;
import com.nhnacademy.bookstoreuserapi.pointtype.repository.PointTypeRepository;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.nhnacademy.bookstoreuserapi.common.exception.OwnerShipValidator.validate;


@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final PointTypeRepository pointTypeRepository;

    @Override
    @Transactional
    public ResponsePoint savePoint(String userId, PointCreateRequest pointCreateRequest) {
        validate(userId, pointCreateRequest.userId());

        User user = userRepository.findByUserId(pointCreateRequest.userId());

        if (user == null) {
            throw new UserNotFoundException(pointCreateRequest.userId());
        }
        Point point = new Point();
        point.setUser(user);
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
    public Page<ResponsePoint> findAll(String userId, Pageable pageable) {
        if (!userRepository.existsByUserId(userId)) {
            throw new UserNotFoundException(userId);
        }
        return pointRepository.findPointByUserId(userId, pageable);
    }

}
