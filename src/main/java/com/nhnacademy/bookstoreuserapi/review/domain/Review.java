package com.nhnacademy.bookstoreuserapi.review.domain;


import com.nhnacademy.bookstoreuserapi.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    @Column(nullable = false)
    private int evaluationScore;

    @Column(nullable = false)
    private String reviewContent;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime reviewedAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @Column(nullable = false)
    private long bookId;
    //아마 bookId를 받지 말고 bookId가 들어있는 주문 내역을 받아서 거기서 참조해서 할 수 있게끔? 그러면 구매한 책에 대해서만 리뷰를 작성할 수 있게끔 할 수 있을 것 같음

    public Review(ReviewCreateRequest review, User user) {
        this.evaluationScore = review.evaluationScore();
        this.reviewContent = review.reviewContent();
        this.reviewImages = convertToReviewImages(review.imageUrls());
        this.reviewImages.forEach(img -> img.setReview(this));
        this.reviewedAt = LocalDateTime.now();
        this.updatedAt = null;
        this.user = user;
        this.bookId = review.bookId();
    }

    private List<ReviewImage> convertToReviewImages(List<String> imageUrls) {
        List<ReviewImage> images = new ArrayList<>();
        if (imageUrls == null || imageUrls.isEmpty()) {
            return images;
        }

        for (String url : imageUrls) {
            ReviewImage image = new ReviewImage();
            image.setImageUrl(url);
            image.setUploadedAt(LocalDateTime.now());
            image.setReview(this); // 연관관계 설정
            images.add(image);
        }
        return images;
    }
}
