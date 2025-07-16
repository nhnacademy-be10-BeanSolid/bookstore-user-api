package com.nhnacademy.bookstoreuserapi.guest.service.impl;

import com.nhnacademy.bookstoreuserapi.guest.domain.Guest;
import com.nhnacademy.bookstoreuserapi.guest.dto.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.dto.response.ResponseGuest;
import com.nhnacademy.bookstoreuserapi.guest.exception.GuestAlreadyExistsException;
import com.nhnacademy.bookstoreuserapi.guest.exception.GuestNotFoundException;
import com.nhnacademy.bookstoreuserapi.guest.repository.GuestRepository;
import com.nhnacademy.bookstoreuserapi.guest.service.GuestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseGuest getGuest(Long orderId) {
        Guest guest = guestRepository.findByOrderId(orderId)
                .orElseThrow(() -> new GuestNotFoundException(orderId));
        return new ResponseGuest(guest.getGuestId(), guest.getOrderId());
    }

    @Override
    @Transactional
    public ResponseGuest addGuest(GuestCreateRequest guestCreateRequest) {
        if (guestRepository.findByOrderId(guestCreateRequest.orderId()).isPresent()) {
            throw new GuestAlreadyExistsException(guestCreateRequest.orderId());
        }
        String encodedPassword = passwordEncoder.encode(guestCreateRequest.guestPassword());
        Guest guest = new Guest(encodedPassword, guestCreateRequest.orderId());
        Guest savedGuest = guestRepository.save(guest);
        return new ResponseGuest(savedGuest.getGuestId(), savedGuest.getOrderId());
    }

    @Override
    @Transactional
    public void deleteGuest(Long orderId) {
        Guest guest = guestRepository.findByOrderId(orderId)
                .orElseThrow(() -> new GuestNotFoundException(orderId));
        guestRepository.delete(guest);
    }

    @Override
    public String getGuestEncodedPassword(Long orderId) {
        Guest guest = guestRepository.findByOrderId(orderId)
                .orElseThrow(() -> new GuestNotFoundException(orderId));
        return guest.getGuestPassword();
    }
}
