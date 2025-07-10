package com.nhnacademy.bookstoreuserapi.adapter;

import com.nhnacademy.bookstoreuserapi.user.domain.UserOrderAmountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "order-api", contextId = "orderAdapter")
public interface OrderAdapter {

    @GetMapping("internal/orders")
    ResponseEntity<List<UserOrderAmountResponse>> getOrderAmountGroupByUserLastThreeMonth();
}
