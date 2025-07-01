package com.nhnacademy.bookstoreuserapi.pointtype.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePointType {

    private Long typeId;
    private String typeName;
    private Integer earningPoint;
    private Integer earningRate;
    private String gradeName;
}
