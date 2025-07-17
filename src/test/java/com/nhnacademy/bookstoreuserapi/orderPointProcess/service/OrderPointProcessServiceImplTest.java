package com.nhnacademy.bookstoreuserapi.orderPointProcess.service;

import com.nhnacademy.bookstoreuserapi.orderPointProcess.dto.PointType;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.dto.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.dto.request.OrderPointPlusProcessRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUser;
import com.nhnacademy.bookstoreuserapi.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderPointProcessServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private PointService pointService;

    @InjectMocks
    private OrderPointProcessServiceImpl orderPointProcessService;

    @Test
    @DisplayName("주문 포인트 적립 처리 성공")
    void testOrderPointPlusProcess() {
        // given
        Long userNo = 1L;
        Long orderNo = 100L;
        int point = 500;
        PointType pointType = PointType.ORDER;
        OrderPointPlusProcessRequest request = new OrderPointPlusProcessRequest(orderNo, point, pointType);

        ResponseUser responseUser = new ResponseUser(1L, "testUser", "password", "Test User", "010-1234-5678", "test@example.com", LocalDate.now(), 1000, true, "ACTIVE", LocalDateTime.now(), LocalDateTime.now(), "USER");
        when(userService.getUserByUserNo(userNo)).thenReturn(responseUser);

        // when
        orderPointProcessService.orderPointPlusProcess(userNo, request);

        // then
        verify(userService, times(1)).getUserByUserNo(userNo);
        verify(userService, times(1)).plusPoint(userNo, point);
        verify(pointService, times(1)).savePoint(eq(responseUser.getUserId()), any(PointCreateRequest.class));
    }

    @Test
    @DisplayName("주문 포인트 사용 처리 성공")
    void testOrderPointMinusProcess() {
        // given
        Long userNo = 1L;
        Long orderNo = 200L;
        int point = 300;
        PointType pointType = PointType.ORDER;
        OrderPointMinusProcessRequest request = new OrderPointMinusProcessRequest(orderNo, point, pointType);

        ResponseUser responseUser = new ResponseUser(1L, "testUser", "password", "Test User", "010-1234-5678", "test@example.com", LocalDate.now(), 1000, true, "ACTIVE", LocalDateTime.now(), LocalDateTime.now(), "USER");
        when(userService.getUserByUserNo(userNo)).thenReturn(responseUser);

        // when
        orderPointProcessService.orderPointMinusProcess(userNo, request);

        // then
        verify(userService, times(1)).getUserByUserNo(userNo);
        verify(userService, times(1)).minusPoint(userNo, point);
        verify(pointService, times(1)).savePoint(eq(responseUser.getUserId()), any(PointCreateRequest.class));
    }
}