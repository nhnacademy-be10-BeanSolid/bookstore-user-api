package com.nhnacademy.bookstoreuserapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;

@FeignClient(name = "gateway-service", fallback = CouponClient.CouponClientFallback.class)
public interface CouponClient {

    @PostMapping("/coupon-api/coupons/users/{userNo}/issue-welcome")
    ResponseEntity<Void> issueWelcomeCoupon(@PathVariable("userNo") String userNo);

    @Slf4j
    @Component
    class CouponClientFallback implements CouponClient {
        @Override
        public ResponseEntity<Void> issueWelcomeCoupon(String userNo) {
            log.error("Fallback: Welcome coupon issuance failed for userNo={}", userNo);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
