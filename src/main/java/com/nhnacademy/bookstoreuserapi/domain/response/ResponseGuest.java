package com.nhnacademy.bookstoreuserapi.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGuest {

    private String guestPassword;
    private String guestName;
    private String guestPhoneNumber;
    private String guestAddress;
    private String guestEmail;
}
