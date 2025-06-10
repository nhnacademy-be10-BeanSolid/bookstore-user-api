package com.nhnacademy.bookstoreuserapi.controller;


import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestReview;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestReview;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseReview;
import com.nhnacademy.bookstoreuserapi.service.impl.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewServiceImpl reviewService;

    @PostMapping
    public ResponseReview addReview(@RequestBody SignUpRequestReview review){
        return reviewService.addReview(review);
    }

    @PutMapping("/{reviewId}")
    public ResponseReview updateReview(@PathVariable long reviewId, @RequestBody EditRequestReview review) {
        return reviewService.editReview(reviewId, review);
    }

    @GetMapping("/{reviewId}")
    public ResponseReview getReview(@PathVariable long reviewId) {
        return reviewService.getReview(reviewId);
    }

    @GetMapping("/user/{userId}")
    public List<ResponseReview> getReviewByUserId(@PathVariable String userId) {
        return reviewService.getReviewsByUserId(userId);
    }
    @GetMapping("/book/{bookId}")
    public List<ResponseReview> getReviewByBookId(@PathVariable long bookId) {
        return reviewService.getReviewsByBookId(bookId);
    }

}
