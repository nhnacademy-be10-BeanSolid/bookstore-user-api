package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.Review;
import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestReview;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestReview;
import com.nhnacademy.bookstoreuserapi.exception.InvalidDataException;
import com.nhnacademy.bookstoreuserapi.exception.InvalidReviewDataException;
import com.nhnacademy.bookstoreuserapi.exception.ReviewAlreadyExistsBookException;
import com.nhnacademy.bookstoreuserapi.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {
    @Mock
    ReviewRepository reviewRepository;

    @InjectMocks
    ReviewServiceImpl reviewService;

    @Test
    void addReview() {
        SignUpRequestReview signUpRequestReview = new SignUpRequestReview(5, "Great book!", "", "user123", 1L);
        Review review = new Review(signUpRequestReview);
        Mockito.when(reviewRepository.findByUserIdAndBookId(review.getUserId(), review.getBookId())).thenReturn(null);
        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        reviewService.addReview(signUpRequestReview);
        Mockito.verify(reviewRepository, Mockito.times(1)).findByUserIdAndBookId(review.getUserId(), review.getBookId());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any(Review.class));
    }

    @Test
    void addReviewFail() {
        SignUpRequestReview signUpRequestReview = new SignUpRequestReview(5, "Great book!", "", "user123", 1L);
        Review review = new Review(signUpRequestReview);
        Mockito.when(reviewRepository.findByUserIdAndBookId(review.getUserId(), review.getBookId())).thenReturn(review);

        Assertions.assertThrows(ReviewAlreadyExistsBookException.class, () -> reviewService.addReview(signUpRequestReview));

        Mockito.verify(reviewRepository, Mockito.times(1)).findByUserIdAndBookId(review.getUserId(), review.getBookId());
    }

    @Test
    void addReviewInvalidData() {
        SignUpRequestReview signUpRequestReview = new SignUpRequestReview(0, "", "", null, 0L);

        Assertions.assertThrows(InvalidReviewDataException.class, () -> reviewService.addReview(signUpRequestReview));
        Mockito.verify(reviewRepository, Mockito.never()).findByUserIdAndBookId(Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any(Review.class));
    }

    @Test
    void addReviewInvalidDataNull() {
        Assertions.assertThrows(InvalidReviewDataException.class, () -> reviewService.addReview(null));
        Mockito.verify(reviewRepository, Mockito.never()).findByUserIdAndBookId(Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any(Review.class));
    }

    @Test
    void addReviewInvalidDataEmpty() {
        SignUpRequestReview signUpRequestReview = new SignUpRequestReview(0, "", "", "", 0L);

        Assertions.assertThrows(InvalidReviewDataException.class, () -> reviewService.addReview(signUpRequestReview));
        Mockito.verify(reviewRepository, Mockito.never()).findByUserIdAndBookId(Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any(Review.class));
    }

    @Test
    void editReview() {
        long reviewId = 1L;
        Review existingReview = new Review(new SignUpRequestReview(5, "Great book!", "", "user123", 1L));
        existingReview.setReviewId(reviewId);

        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        Mockito.when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(existingReview);

        reviewService.editReview(reviewId, new EditRequestReview(4, "Updated review", ""));

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    void editReviewFail() {
        long reviewId = 1L;
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ReviewNotFoundException.class, () -> reviewService.editReview(reviewId, new EditRequestReview(4, "Updated review", "")));

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    void getReview() {
        long reviewId = 1L;
        Review existingReview = new Review(new SignUpRequestReview(5, "Great book!", "", "user123", 1L));
        existingReview.setReviewId(reviewId);

        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        reviewService.getReview(reviewId);
        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    void getReviewFail() {
        long reviewId = 1L;
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ReviewNotFoundException.class, () -> reviewService.getReview(reviewId));

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewId);
    }

    @Test
    void getReviewByUserId() {
        Review review = new Review(5, "Great book!", "", "user123", 1L);
        Mockito.when(reviewRepository.findAllByUserId("user123")).thenReturn(Collections.singletonList(review));
        reviewService.getReviewsByUserId("user123");
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByUserId("user123");
    }

    @Test
    void getReviewsByUserId_NullUserId_ThrowsException() {
        Assertions.assertThrows(InvalidDataException.class, () -> reviewService.getReviewsByUserId(null));
        Mockito.verify(reviewRepository, Mockito.never()).findAllByUserId(Mockito.any());
    }

    @Test
    void getReviewsByUserId_EmptyUserId_ThrowsException() {
        Assertions.assertThrows(InvalidDataException.class, () -> reviewService.getReviewsByUserId(""));
        Mockito.verify(reviewRepository, Mockito.never()).findAllByUserId(Mockito.any());
    }


    @Test
    void getReviewByBookId() {
        long bookId = 1L;
        Review review = new Review(5, "Great book!", "", "user123", bookId);
        Mockito.when(reviewRepository.findAllByBookId(bookId)).thenReturn(Collections.singletonList(review));
        reviewService.getReviewsByBookId(bookId);
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByBookId(bookId);
    }

    @Test
    void getReviewsByBookId_InvalidBookId_ThrowsException() {
        Assertions.assertThrows(InvalidDataException.class, () -> reviewService.getReviewsByBookId(-1L));
        Mockito.verify(reviewRepository, Mockito.never()).findAllByBookId(Mockito.anyLong());
    }
}
