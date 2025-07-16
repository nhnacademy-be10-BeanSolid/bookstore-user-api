package com.nhnacademy.bookstoreuserapi.review.controller;


import com.nhnacademy.bookstoreuserapi.common.annotation.AuthenticatedUserId;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.review.domain.*;
import com.nhnacademy.bookstoreuserapi.review.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/me")
    public ResponseEntity<ResponseReview> addReview(@AuthenticatedUserId  String userId, @Valid @RequestBody ReviewCreateRequest review, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(userId, review));
    }

    @PutMapping("/me/{reviewId}")
    public ResponseEntity<ResponseReview> updateReview(@AuthenticatedUserId String userId, @PathVariable @Min(1) long reviewId, @Valid @RequestBody ReviewUpdateRequest review, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.ok().body(reviewService.editReview(userId, reviewId, review));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ResponseReview> getReview(@PathVariable @Min(1) long reviewId) {
        return ResponseEntity.ok().body(reviewService.getReview(reviewId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ResponseSimpleReviewByUser>> getReviewByUserId(@PathVariable @NotBlank @Size(max = 20) String userId, Pageable pageable) {
        return ResponseEntity.ok().body(reviewService.getReviewsByUserId(userId, pageable));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Page<ResponseSimpleReview>> getReviewByBookId(@PathVariable @Min(1) long bookId, Pageable pageable) {
        return ResponseEntity.ok().body(reviewService.getReviewsByBookId(bookId, pageable));
    }

    @GetMapping("/book/{bookId}/count")
    public ResponseEntity<Long> countReviewsByBookId(@PathVariable @Min(1) long bookId) {
        return ResponseEntity.ok().body(reviewService.countReviewsByBookId(bookId));
    }

    @GetMapping("/book/{bookId}/average-score")
    public ResponseEntity<Double> getAverageEvaluationScoreByBookId(@PathVariable @Min(1) long bookId) {
        return ResponseEntity.ok().body(reviewService.getAverageEvaluationScoreByBookId(bookId));
    }

}
