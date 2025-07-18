package com.nhnacademy.bookstoreuserapi.adapter;

import com.nhnacademy.bookstoreuserapi.user.domain.UserOrderAmountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "order-api", contextId = "orderAdapter")
public interface OrderAdapter {

    @GetMapping("internal/orders")
    ResponseEntity<List<UserOrderAmountResponse>> getOrderAmountGroupByUserLastThreeMonth();

    @GetMapping("internal/orders/exists")
    boolean validatePurchase(@RequestParam Long userNo, @RequestParam Long bookId);
}
