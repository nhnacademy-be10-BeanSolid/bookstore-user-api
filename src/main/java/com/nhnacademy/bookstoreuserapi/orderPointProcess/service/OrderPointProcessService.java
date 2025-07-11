package com.nhnacademy.bookstoreuserapi.orderPointProcess.service;

import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointPlusProcessRequest;

public interface OrderPointProcessService {

    void orderPointPlusProcess(OrderPointPlusProcessRequest request);

    void orderPointMinusProcess(OrderPointMinusProcessRequest request);
}
