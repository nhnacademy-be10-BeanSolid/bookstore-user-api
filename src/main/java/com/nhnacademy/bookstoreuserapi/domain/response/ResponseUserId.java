package com.nhnacademy.bookstoreuserapi.domain.response;


import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserId {
    private String userId;

    public ResponseUserId(User user) {
        this.userId = user.getUserId();
    }
}
