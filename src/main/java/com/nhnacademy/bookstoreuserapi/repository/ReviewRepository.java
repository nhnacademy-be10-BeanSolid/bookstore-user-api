package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import com.nhnacademy.bookstoreuserapi.repository.queryfactory.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    Review findByUser_UserIdAndBookId(String userId, long bookId);

}
