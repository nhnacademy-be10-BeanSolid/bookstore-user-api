package com.nhnacademy.bookstoreuserapi.user.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserId {
    private String userId;
    private Long userNo;

    public ResponseUserId(User user) {
        this.userId = user.getUserId();
    }
}
