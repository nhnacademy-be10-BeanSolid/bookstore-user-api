package com.nhnacademy.bookstoreuserapi.guest.service.impl;

import com.nhnacademy.bookstoreuserapi.guest.domain.Guest;
import com.nhnacademy.bookstoreuserapi.guest.domain.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.domain.GuestUpdateRequest;
import com.nhnacademy.bookstoreuserapi.guest.domain.ResponseGuest;
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
    public ResponseGuest getGuest(String guestEmail) {

        Guest guest = guestRepository.findByGuestEmail(guestEmail);

        if(guest == null) {
            throw new GuestNotFoundException(guestEmail);
        }

       return new ResponseGuest(
                guest.getGuestPassword(),
                guest.getGuestName(),
                guest.getGuestPhoneNumber(),
                guest.getGuestAddress(),
                guest.getGuestEmail()
        );
    }

    @Override
    @Transactional
    public ResponseGuest addGuest(GuestCreateRequest guestCreateRequest) {

        Guest guest = new Guest(guestCreateRequest);

        guest.setGuestPassword(passwordEncoder.encode(guest.getGuestPassword()));

        Guest saveGuest = guestRepository.save(guest);

        return new ResponseGuest(
                saveGuest.getGuestPassword(),
                saveGuest.getGuestName(),
                saveGuest.getGuestPhoneNumber(),
                saveGuest.getGuestAddress(),
                saveGuest.getGuestEmail()
        );
    }

    @Override
    @Transactional
    public void deleteGuest(String guestEmail) {

        Guest guest = guestRepository.findByGuestEmail(guestEmail);

        if(guest == null) {
            throw new GuestNotFoundException(guestEmail);
        }

        guestRepository.deleteByGuestEmail(guestEmail);
    }

    @Override
    @Transactional
    public ResponseGuest updateGuest(String guestEmail, GuestUpdateRequest guestUpdateRequest) {

        Guest guest = guestRepository.findByGuestEmail(guestEmail);

        if(guest == null) {
            throw new GuestNotFoundException(guestEmail);
        }

        guest.setGuestPassword(passwordEncoder.encode(guestUpdateRequest.guestPassword()));
        guest.setGuestName(guestUpdateRequest.guestName());
        guest.setGuestPhoneNumber(guestUpdateRequest.guestPhoneNumber());
        guest.setGuestAddress(guestUpdateRequest.guestAddress());

        return new ResponseGuest(
                guest.getGuestPassword(),
                guest.getGuestName(),
                guest.getGuestPhoneNumber(),
                guest.getGuestAddress(),
                guestEmail
        );
    }
}
