package com.nhnacademy.bookstoreuserapi.review.repository;

import com.nhnacademy.bookstoreuserapi.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
//    void deleteAll(List<ReviewImage> reviewImages);
}
