package com.nhnacademy.bookstoreuserapi.adapter;

import com.nhnacademy.bookstoreuserapi.user.domain.UserOrderAmountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "gateway-service", contextId = "orderAdapter")
public interface OrderAdapter {

    @GetMapping("order-api/internal/orders")
    ResponseEntity<List<UserOrderAmountResponse>> getOrderAmountGroupByUserLastThreeMonth();
}
