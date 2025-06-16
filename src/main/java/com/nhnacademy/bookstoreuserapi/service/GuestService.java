package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseGuest;

public interface GuestService {

    ResponseGuest getGuestByguestEmailAndGuestPassword(String guestEmail, String guestPassword);

    ResponseGuest addGuest(GuestCreateRequest guestCreateRequest);

    void deleteGuest(String guestEmail);

}
