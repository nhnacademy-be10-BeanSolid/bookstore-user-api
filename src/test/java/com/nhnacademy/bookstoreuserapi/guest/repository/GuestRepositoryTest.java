package com.nhnacademy.bookstoreuserapi.guest.repository;

import com.nhnacademy.bookstoreuserapi.common.config.QuerydslConfig;
import com.nhnacademy.bookstoreuserapi.guest.domain.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class GuestRepositoryTest {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private EntityManager entityManager;

    private Guest guest;
    private Long orderId;
    private String guestPassword;

    @BeforeEach
    void setUp() {
        orderId = 12345L;
        guestPassword = "testPassword123!";
        guest = new Guest(guestPassword, orderId);
    }

    @Test
    void findByOrderId_found() {
        guestRepository.save(guest);
        entityManager.flush();
        entityManager.clear();

        Optional<Guest> foundGuest = guestRepository.findByOrderId(orderId);

        assertTrue(foundGuest.isPresent());
        assertEquals(orderId, foundGuest.get().getOrderId());
        assertEquals(guestPassword, foundGuest.get().getGuestPassword());
    }

    @Test
    void findByOrderId_notFound() {
        Optional<Guest> foundGuest = guestRepository.findByOrderId(99999L);

        assertFalse(foundGuest.isPresent());
    }

    @Test
    void saveGuest() {
        Guest savedGuest = guestRepository.save(guest);
        entityManager.flush();
        entityManager.clear();

        assertNotNull(savedGuest.getGuestId());
        Optional<Guest> foundGuest = guestRepository.findById(savedGuest.getGuestId());
        assertTrue(foundGuest.isPresent());
        assertEquals(orderId, foundGuest.get().getOrderId());
    }

    @Test
    void deleteGuest() {
        guestRepository.save(guest);
        entityManager.flush();
        entityManager.clear();

        guestRepository.delete(guest);
        entityManager.flush();
        entityManager.clear();

        Optional<Guest> foundGuest = guestRepository.findByOrderId(orderId);
        assertFalse(foundGuest.isPresent());
    }
}
