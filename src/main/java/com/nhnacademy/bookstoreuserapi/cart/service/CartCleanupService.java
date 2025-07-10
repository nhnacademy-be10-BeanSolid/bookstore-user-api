package com.nhnacademy.bookstoreuserapi.cart.service;

import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import com.nhnacademy.bookstoreuserapi.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartCleanupService {

    private final CartRepository cartRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void removeOrphanGuestCarts() {
        // 생성일 30일 경과
        cartRepository.deleteByOwnerTypeAndCreatedAtBefore(
                OwnerType.GUEST, LocalDateTime.now().minusDays(30)
        );

        // 수정일 7일 경과
        cartRepository.deleteByOwnerTypeAndUpdatedAtBefore(
                OwnerType.GUEST, LocalDateTime.now().minusDays(7)
        );
    }
}
