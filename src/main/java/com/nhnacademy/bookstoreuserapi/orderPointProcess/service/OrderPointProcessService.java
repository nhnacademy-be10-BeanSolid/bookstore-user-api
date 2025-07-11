package com.nhnacademy.bookstoreuserapi.orderPointProcess.service;

import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointPlusProcessRequest;

public interface OrderPointProcessService {

    void orderPointPlusProcess(String userId, OrderPointPlusProcessRequest request);

    void orderPointMinusProcess(String userId, OrderPointMinusProcessRequest request);
}
