package com.nhnacademy.bookstoreuserapi.guest.service;

import com.nhnacademy.bookstoreuserapi.guest.dto.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.dto.response.ResponseGuest;

public interface GuestService {

    ResponseGuest getGuest(Long orderId);

    ResponseGuest addGuest(GuestCreateRequest guestCreateRequest);

    void deleteGuest(Long orderId);

    String getGuestEncodedPassword(Long orderId);
}
