package com.nhnacademy.bookstoreuserapi.orderPointProcess.service;

import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.PointType;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointPlusProcessRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUser;
import com.nhnacademy.bookstoreuserapi.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderPointProcessServiceImpl implements OrderPointProcessService {

    private final UserService userService;
    private final PointService pointService;

    @Override
    public void orderPointPlusProcess(Long userNo, OrderPointPlusProcessRequest request) {
        Long orderNo = request.orderNo();
        int point = request.point();
        PointType pointType = request.pointType();

        ResponseUser responseUser = userService.getUserByUserNo(userNo);
        String userId = responseUser.getUserId();
        userService.plusPoint(userNo, point);

        Long typeId = pointType.getId();

        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                                                userId,
                                                typeId,
                                                orderNo,
                                                LocalDateTime.now(),
                point + " 적립");

        pointService.savePoint(userId, pointCreateRequest);
    }

    @Override
    public void orderPointMinusProcess(Long userNo, OrderPointMinusProcessRequest request) {
        Long orderNo = request.orderNo();
        int point = request.point();
        PointType pointType = request.pointType();

        ResponseUser responseUser = userService.getUserByUserNo(userNo);
        String userId = responseUser.getUserId();
        userService.minusPoint(userNo, point);

        Long typeId = pointType.getId();

        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                userId,
                typeId,
                orderNo,
                LocalDateTime.now(),
                point + " 차감");

        pointService.savePoint(userId, pointCreateRequest);
    }
}
