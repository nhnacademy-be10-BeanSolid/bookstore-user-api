package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByUserIdAndBookId(String userId, long bookId);

    List<Review> findAllByUserId(String userId);

    List<Review> findAllByBookId(long bookId);
}
