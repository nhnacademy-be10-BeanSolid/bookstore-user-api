package com.nhnacademy.bookstoreuserapi.guest.service;

import com.nhnacademy.bookstoreuserapi.guest.dto.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.dto.request.GuestUpdateRequest;
import com.nhnacademy.bookstoreuserapi.guest.dto.response.ResponseGuest;

public interface GuestService {

    ResponseGuest getGuest(String guestEmail);

    ResponseGuest addGuest(GuestCreateRequest guestCreateRequest);

    void deleteGuest(String guestEmail);

    ResponseGuest updateGuest(String guestEmail, GuestUpdateRequest guestUpdateRequest);
}
