package com.nhnacademy.bookstoreuserapi.guest.exception;

public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(String guestEmail) {
      super("Guest Email : " + guestEmail + " not found");
    }
}
