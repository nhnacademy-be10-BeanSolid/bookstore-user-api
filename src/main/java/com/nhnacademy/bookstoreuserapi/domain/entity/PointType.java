package com.nhnacademy.bookstoreuserapi.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "point_type")
@Getter
@Setter
@AllArgsConstructor
public class PointType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "type_name", nullable = false, unique = true)
    private String typeName;

    @Column(name = "earning_point")
    private Integer earningPoint;

    @Column(name = "earning_rate")
    private Integer earningRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_name")
    private UserGrade userGrade;
}
