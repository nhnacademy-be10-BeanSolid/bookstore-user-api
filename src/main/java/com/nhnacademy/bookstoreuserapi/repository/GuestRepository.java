package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    Guest findByGuestEmail(String guestEmail);

    @Modifying(clearAutomatically = true)
    @Query("update Guest g set g.guestPassword = :password, g.guestName = :guestName, g.guestPhoneNumber = :guestPhoneNumber," +
            "g.guestAddress = :guestAddress where g.guestEmail = :guestEmail")
    void updateGuest(String password, String guestName, String guestPhoneNumber, String guestAddress, String guestEmail);

    void deleteByGuestEmail(String guestEmail);
}
