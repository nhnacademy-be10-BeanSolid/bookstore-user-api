package com.nhnacademy.bookstoreuserapi.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointTypeCreateRequest {

    private String typeName;
    private Long earningPoint;
    private int earningRate;
    private String gradeName;
}
