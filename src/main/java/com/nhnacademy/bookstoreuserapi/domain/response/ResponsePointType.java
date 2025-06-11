package com.nhnacademy.bookstoreuserapi.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePointType {

    private Long typeId;
    private String typeName;
    private Long earningPoint;
    private int earningRate;
    private String gradeName;
}
