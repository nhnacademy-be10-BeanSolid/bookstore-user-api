package com.nhnacademy.bookstoreuserapi.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "point")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long pointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 관계매핑필요
    @Column(name = "type_id", nullable = false)
    private Long typeId;

    // 관계매핑필요
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "earned_and_used_at", nullable = false)
    private LocalDateTime earnedAndUsedAt;

    @Column(name = "earned_and_used_point", nullable = false)
    private Long earnedAndUsedPoint;
}
