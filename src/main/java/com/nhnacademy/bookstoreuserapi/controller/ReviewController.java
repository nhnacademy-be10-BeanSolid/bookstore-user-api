package com.nhnacademy.bookstoreuserapi.controller;


import com.nhnacademy.bookstoreuserapi.domain.request.ReviewUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.ReviewCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseReview addReview(@Valid @RequestBody ReviewCreateRequest review, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return reviewService.addReview(review);
    }

    @PutMapping("/{reviewId}")
    public ResponseReview updateReview(@PathVariable @Min(1) long reviewId, @Valid @RequestBody ReviewUpdateRequest review, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return reviewService.editReview(reviewId, review);
    }

    @GetMapping("/{reviewId}")
    public ResponseReview getReview(@PathVariable @Min(1) long reviewId) {
        return reviewService.getReview(reviewId);
    }

    @GetMapping("/user/{userId}")
    public Page<ResponseReview> getReviewByUserId(@PathVariable @NotBlank @Size(max = 20) String userId, Pageable pageable) {
        return reviewService.getReviewsByUserId(userId, pageable);
    }

    @GetMapping("/book/{bookId}")
    public Page<ResponseReview> getReviewByBookId(@PathVariable @Min(1) long bookId, Pageable pageable) {
        return reviewService.getReviewsByBookId(bookId, pageable);
    }

}
