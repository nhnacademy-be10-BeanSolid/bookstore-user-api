package com.nhnacademy.bookstoreuserapi.domain.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserGrade {
    private String gradeName;
    private long requiredMoney;

}
