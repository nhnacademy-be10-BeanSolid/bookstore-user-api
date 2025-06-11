package com.nhnacademy.bookstoreuserapi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "point_type")
@Getter
@Setter
public class PointType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "type_name", nullable = false)
    private String typeName;

    @Column(name = "earning_point", nullable = false)
    private Long earningPoint;

    @Column(name = "earning_rate")
    private int earningRate;

    // 관계 매핑 필요
    @Column(name = "grade_name", nullable = false)
    private String gradeName;
}
