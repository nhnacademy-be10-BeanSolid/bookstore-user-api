package com.nhnacademy.bookstoreuserapi.domain.entity;


import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestReview;
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
    //아마 bookId를 받지 말고 bookId가 들어있는 주문 내역을 받아서 거기서 참조해서 할 수 있게끔? 그러면 구매한 책에 대해서만 리뷰를 작성할 수 있게끔 할 수 있을 것 같음

    public Review(SignUpRequestReview review){
        this.evaluationScore = review.getEvaluationScore();
        this.reviewContent = review.getReviewContent();
        this.reviewPhoto = review.getReviewPhoto();
        this.reviewedAt = ZonedDateTime.now();
        this.updatedAt = null;
        this.userId = review.getUserId();
        this.bookId = review.getBookId();
    }
}
