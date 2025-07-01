package com.nhnacademy.bookstoreuserapi.review.repository;

import com.nhnacademy.bookstoreuserapi.review.domain.Review;
import com.nhnacademy.bookstoreuserapi.review.repository.queryfactory.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    Review findByUser_UserIdAndBookId(String userId, long bookId);

}
