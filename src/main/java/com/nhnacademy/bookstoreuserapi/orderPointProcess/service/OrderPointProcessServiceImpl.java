package com.nhnacademy.bookstoreuserapi.orderPointProcess.service;

import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointPlusProcessRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUser;
import com.nhnacademy.bookstoreuserapi.user.service.UserService;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
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
    private final PointTypeService pointTypeService;

    @Override
    public void orderPointPlusProcess(OrderPointPlusProcessRequest request) {

        Long orderNo = request.orderNo();
        int purePrice = request.purePrice();

        ResponseUser responseUser = userService.getUser(request.userId());

        ResponsePointType responsePointType = pointTypeService.getEarningRateByGradeNameAndTypeName(UserGrade.Grade.valueOf(responseUser.getUserGradeName()));

        int plusPoint = purePrice + (purePrice * responsePointType.getEarningRate() / 100);

        String userId = responseUser.getUserId();

        userService.plusPoint(userId, plusPoint);

        PointCreateRequest pointCreateRequest = new PointCreateRequest(userId,
                                                responsePointType.getTypeId(),
                                                orderNo,
                                                LocalDateTime.now(),
                plusPoint + " 적립");

        pointService.savePoint(userId, pointCreateRequest);
    }

    @Override
    public void orderPointMinusProcess(OrderPointMinusProcessRequest request) {

        Long orderNo = request.orderNo();

        ResponseUser responseUser = userService.getUser(request.userId());

        String userId = responseUser.getUserId();

        int minusPoint = request.usePoint();

        Long typeId = pointTypeService.getTypeIdByName("주문");

        userService.minusPoint(responseUser.getUserId(), minusPoint);

        PointCreateRequest pointCreateRequest = new PointCreateRequest(userId,
                typeId,
                orderNo,
                LocalDateTime.now(),
                minusPoint + " 차감");

        pointService.savePoint(userId, pointCreateRequest);
    }
}
