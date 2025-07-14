package com.nhnacademy.bookstoreuserapi.guest.repository;

import com.nhnacademy.bookstoreuserapi.guest.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByOrderId(Long orderId);
}
