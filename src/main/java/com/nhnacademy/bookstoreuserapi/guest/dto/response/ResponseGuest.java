package com.nhnacademy.bookstoreuserapi.guest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGuest {
    private Long guestId;
    private Long orderId;
}
