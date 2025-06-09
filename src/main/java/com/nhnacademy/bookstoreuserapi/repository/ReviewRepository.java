package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByUserIdByBookId(String userId, long bookId);
}
