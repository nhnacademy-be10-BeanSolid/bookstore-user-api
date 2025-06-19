package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GuestRepository extends JpaRepository<Guest, Long> {

    Guest findByGuestEmail(String guestEmail);

    void deleteByGuestEmail(String guestEmail);
}
