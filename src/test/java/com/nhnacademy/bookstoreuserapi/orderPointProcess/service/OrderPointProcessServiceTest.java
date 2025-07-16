//package com.nhnacademy.bookstoreuserapi.orderPointProcess.service;
//
//import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointMinusProcessRequest;
//import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointPlusProcessRequest;
//import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
//import com.nhnacademy.bookstoreuserapi.point.service.PointService;
//import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
//import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
//import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUser;
//import com.nhnacademy.bookstoreuserapi.user.service.UserService;
//import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class OrderPointProcessServiceTest {
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private PointService pointService;
//
//    @Mock
//    private PointTypeService pointTypeService;
//
//    @InjectMocks
//    private OrderPointProcessServiceImpl orderPointProcessService;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void orderPointPlusProcess_success() {
//        Long userNo = 1L;
//        Long orderNo = 10L;
//        int purePrice = 10000;
//        String userGradeName = "BASIC";
//        String userId = "tester";
//        int earningRate = 4;
//        Long typeId = 2L;
//
//        OrderPointPlusProcessRequest request = mock(OrderPointPlusProcessRequest.class);
//        when(request.orderNo()).thenReturn(orderNo);
//        when(request.purePrice()).thenReturn(purePrice);
//
//        ResponseUser responseUser = mock(ResponseUser.class);
//        when(userService.getUserByUserNo(userNo)).thenReturn(responseUser);
//        when(responseUser.getUserGradeName()).thenReturn(userGradeName);
//        when(responseUser.getUserId()).thenReturn(userId);
//
//        ResponsePointType responsePointType = mock(ResponsePointType.class);
//        when(pointTypeService.getEarningRateByGradeNameAndTypeName(any(UserGrade.Grade.class)))
//                .thenReturn(responsePointType);
//        when(responsePointType.getEarningRate()).thenReturn(earningRate);
//        when(responsePointType.getTypeId()).thenReturn(typeId);
//
//        orderPointProcessService.orderPointPlusProcess(userNo, request);
//
//        int expectedPoints = (int) (purePrice * (earningRate / 100.0));
//
//        verify(userService, times(1)).plusPoint(eq(userNo), eq(expectedPoints));
//        verify(pointService, times(1)).savePoint(eq(userId), any(PointCreateRequest.class));
//    }
//
//    @Test
//    void orderPointMinusProcess_success() {
//        Long userNo = 2L;
//        Long orderNo = 20L;
//        int usePoint = 3000;
//        String userId = "minus-user";
//        Long typeId = 3L;
//
//        OrderPointMinusProcessRequest request = mock(OrderPointMinusProcessRequest.class);
//        when(request.orderNo()).thenReturn(orderNo);
//        when(request.usePoint()).thenReturn(usePoint);
//
//        ResponseUser responseUser = mock(ResponseUser.class);
//        when(userService.getUserByUserNo(userNo)).thenReturn(responseUser);
//        when(responseUser.getUserId()).thenReturn(userId);
//
//        when(pointTypeService.getTypeIdByName(anyString())).thenReturn(typeId);
//
//        orderPointProcessService.orderPointMinusProcess(userNo, request);
//
//        verify(userService, times(1)).minusPoint(eq(userNo), eq(usePoint));
//        verify(pointService, times(1)).savePoint(eq(userId), any(PointCreateRequest.class));
//    }
//}
