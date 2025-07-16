package com.nhnacademy.bookstoreuserapi.guest.service;

import com.nhnacademy.bookstoreuserapi.guest.domain.Guest;
import com.nhnacademy.bookstoreuserapi.guest.dto.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.dto.response.ResponseGuest;
import com.nhnacademy.bookstoreuserapi.guest.exception.GuestAlreadyExistsException;
import com.nhnacademy.bookstoreuserapi.guest.exception.GuestNotFoundException;
import com.nhnacademy.bookstoreuserapi.guest.repository.GuestRepository;
import com.nhnacademy.bookstoreuserapi.guest.service.impl.GuestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private GuestServiceImpl guestService;

    private Guest guest;
    private GuestCreateRequest guestCreateRequest;
    private Long orderId;
    private String rawPassword;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        orderId = 1L;
        rawPassword = "testPassword123!";
        encodedPassword = "encodedTestPassword123!";
        guest = new Guest(1L, encodedPassword, orderId);
        guestCreateRequest = new GuestCreateRequest(rawPassword, orderId);
    }

    @Test
    void getGuest_success() {
        when(guestRepository.findByOrderId(orderId)).thenReturn(Optional.of(guest));

        ResponseGuest result = guestService.getGuest(orderId);

        assertNotNull(result);
        assertEquals(guest.getGuestId(), result.getGuestId());
        assertEquals(guest.getOrderId(), result.getOrderId());
        verify(guestRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void getGuest_notFound() {
        when(guestRepository.findByOrderId(orderId)).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestService.getGuest(orderId));
        verify(guestRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void addGuest_success() {
        when(guestRepository.findByOrderId(orderId)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);

        ResponseGuest result = guestService.addGuest(guestCreateRequest);

        assertNotNull(result);
        assertEquals(guest.getGuestId(), result.getGuestId());
        assertEquals(guest.getOrderId(), result.getOrderId());
        verify(guestRepository, times(1)).findByOrderId(orderId);
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(guestRepository, times(1)).save(any(Guest.class));
    }

    @Test
    void addGuest_alreadyExists() {
        when(guestRepository.findByOrderId(orderId)).thenReturn(Optional.of(guest));

        assertThrows(GuestAlreadyExistsException.class, () -> guestService.addGuest(guestCreateRequest));
        verify(guestRepository, times(1)).findByOrderId(orderId);
        verify(passwordEncoder, never()).encode(anyString());
        verify(guestRepository, never()).save(any(Guest.class));
    }

    @Test
    void deleteGuest_success() {
        when(guestRepository.findByOrderId(orderId)).thenReturn(Optional.of(guest));
        doNothing().when(guestRepository).delete(guest);

        guestService.deleteGuest(orderId);

        verify(guestRepository, times(1)).findByOrderId(orderId);
        verify(guestRepository, times(1)).delete(guest);
    }

    @Test
    void deleteGuest_notFound() {
        when(guestRepository.findByOrderId(orderId)).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestService.deleteGuest(orderId));
        verify(guestRepository, times(1)).findByOrderId(orderId);
        verify(guestRepository, never()).delete(any(Guest.class));
    }

    @Test
    void getGuestEncodedPassword_success() {
        when(guestRepository.findByOrderId(orderId)).thenReturn(Optional.of(guest));

        String result = guestService.getGuestEncodedPassword(orderId);

        assertNotNull(result);
        assertEquals(encodedPassword, result);
        verify(guestRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void getGuestEncodedPassword_notFound() {
        when(guestRepository.findByOrderId(orderId)).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestService.getGuestEncodedPassword(orderId));
        verify(guestRepository, times(1)).findByOrderId(orderId);
    }
}