package com.nhnacademy.bookstoreuserapi.guest.exception;

import com.nhnacademy.bookstoreuserapi.common.exception.NotFoundException;

public class GuestNotFoundException extends NotFoundException {
    public GuestNotFoundException(String guestEmail) {
      super("Guest Email : " + guestEmail + " not found");
    }
}
