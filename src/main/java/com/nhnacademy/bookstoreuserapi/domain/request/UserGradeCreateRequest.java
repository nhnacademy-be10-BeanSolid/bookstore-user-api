package com.nhnacademy.bookstoreuserapi.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGradeCreateRequest {
    private String gradeName;
    private long requiredMoney;
}
