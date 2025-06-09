package com.nhnacademy.bookstoreuserapi.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    @Column(nullable = false)
    private int evaluationScore;

    @Column(nullable = false)
    private String reviewContent;

    @Column
    private String reviewPhoto;

    @Column(nullable = false)
    private ZonedDateTime reviewedAt;

    private ZonedDateTime updatedAt;

//    @ManyToOne    //user 엔티티 생성 후 주석 해제
//    @JoinColumn(name = "user_id", nullable = false)
//    @MapsId("userId")
//    private User user;
    private String userId;

    @Column(nullable = false)
    private long bookId;
}
