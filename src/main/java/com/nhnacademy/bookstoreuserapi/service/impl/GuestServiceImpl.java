package com.nhnacademy.bookstoreuserapi.service.impl;

import com.nhnacademy.bookstoreuserapi.domain.entity.Guest;
import com.nhnacademy.bookstoreuserapi.domain.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.GuestUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseGuest;
import com.nhnacademy.bookstoreuserapi.exception.GuestNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.GuestRepository;
import com.nhnacademy.bookstoreuserapi.service.GuestService;
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

        guestRepository.delete(guest);
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

        guestRepository.updateGuest(guest.getGuestPassword(),
                                    guest.getGuestName(),
                                    guest.getGuestPhoneNumber(),
                                    guest.getGuestAddress(),
                                    guestEmail);

        return new ResponseGuest(
                guest.getGuestPassword(),
                guest.getGuestName(),
                guest.getGuestPhoneNumber(),
                guest.getGuestAddress(),
                guestEmail
        );
    }
}
