package com.nhnacademy.bookstoreuserapi.orderPointProcess.service;

import com.nhnacademy.bookstoreuserapi.orderPointProcess.dto.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.dto.request.OrderPointPlusProcessRequest;

public interface OrderPointProcessService {

    void orderPointPlusProcess(Long userNo, OrderPointPlusProcessRequest request);

    void orderPointMinusProcess(Long userNo, OrderPointMinusProcessRequest request);
}
