package com.nhnacademy.bookstoreuserapi.orderpointprocess.service;

import com.nhnacademy.bookstoreuserapi.orderpointprocess.dto.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderpointprocess.dto.request.OrderPointPlusProcessRequest;

public interface OrderPointProcessService {

    void orderPointPlusProcess(Long userNo, OrderPointPlusProcessRequest request);

    void orderPointMinusProcess(Long userNo, OrderPointMinusProcessRequest request);
}
