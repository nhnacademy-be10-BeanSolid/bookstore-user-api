package com.nhnacademy.bookstoreuserapi.guest.repository;

import com.nhnacademy.bookstoreuserapi.guest.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GuestRepository extends JpaRepository<Guest, Long> {

    Guest findByGuestEmail(String guestEmail);

    void deleteByGuestEmail(String guestEmail);
}
