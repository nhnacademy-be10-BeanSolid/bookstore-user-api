package com.nhnacademy.bookstoreuserapi.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressCreateRequest {
    private String addressNickName;
    private String addressDetail;
    private String userId;
}
