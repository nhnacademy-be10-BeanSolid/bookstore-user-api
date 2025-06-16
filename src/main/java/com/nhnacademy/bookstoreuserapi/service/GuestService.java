package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.GuestUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseGuest;

public interface GuestService {

    ResponseGuest getGuest(String guestEmail);

    ResponseGuest addGuest(GuestCreateRequest guestCreateRequest);

    void deleteGuest(String guestEmail);

    ResponseGuest updateGuest(String guestEmail, GuestUpdateRequest guestUpdateRequest);
}
