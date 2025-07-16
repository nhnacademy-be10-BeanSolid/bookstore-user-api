package com.nhnacademy.bookstoreuserapi.review.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review_images")
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewImageId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @ToString.Exclude
    private Review review;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewImage that)) return false;
        return reviewImageId != null && reviewImageId.equals(that.reviewImageId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
