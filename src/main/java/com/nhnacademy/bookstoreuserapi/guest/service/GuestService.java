package com.nhnacademy.bookstoreuserapi.guest.service;

import com.nhnacademy.bookstoreuserapi.guest.domain.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.domain.GuestUpdateRequest;
import com.nhnacademy.bookstoreuserapi.guest.domain.ResponseGuest;

public interface GuestService {

    ResponseGuest getGuest(String guestEmail);

    ResponseGuest addGuest(GuestCreateRequest guestCreateRequest);

    void deleteGuest(String guestEmail);

    ResponseGuest updateGuest(String guestEmail, GuestUpdateRequest guestUpdateRequest);
}
