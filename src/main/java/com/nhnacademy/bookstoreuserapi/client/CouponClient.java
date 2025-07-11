package com.nhnacademy.bookstoreuserapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@FeignClient(name = "bookstore-coupon-api", fallback = CouponClient.CouponClientFallback.class)
public interface CouponClient {

    @PostMapping("/coupons/users/{userNo}/issue-welcome")
    ResponseEntity<Void> issueWelcomeCoupon(@PathVariable("userNo") String userNo);

    @Component
    @Slf4j
    class CouponClientFallback implements CouponClient {
        @Override
        public ResponseEntity<Void> issueWelcomeCoupon(String userNo) {
            // 쿠폰 서비스 호출 실패 시, 에러 로그를 남기고 null을 반환하여 회원가입은 계속 진행되도록 함 !
            log.error("Welcome coupon issuance failed for userNo: {}. Fallback triggered.", userNo);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
