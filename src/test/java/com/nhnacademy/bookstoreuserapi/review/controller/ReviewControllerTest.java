package com.nhnacademy.bookstoreuserapi.review.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.review.domain.ReviewUpdateRequest;
import com.nhnacademy.bookstoreuserapi.review.domain.ReviewCreateRequest;
import com.nhnacademy.bookstoreuserapi.review.exception.ReviewAlreadyExistsBookException;
import com.nhnacademy.bookstoreuserapi.review.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreuserapi.review.service.ReviewService;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addReview() throws Exception {
        ReviewCreateRequest review = new ReviewCreateRequest(5, "Great book!", "", "user123", 1L);
        Mockito.when(reviewService.addReview("user123", review)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/reviews/me")
                        .header("X-USER-ID", "user123")
                        .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(reviewService, Mockito.times(1)).addReview("user123", review);
    }

    @Test
    void addReviewFailAlreadyExist() throws Exception {
        ReviewCreateRequest review = new ReviewCreateRequest(5, "Great book!", "", "user123", 1L);
        Mockito.when(reviewService.addReview("user123", review)).thenThrow(new ReviewAlreadyExistsBookException("user123", 1L));

        mockMvc.perform(MockMvcRequestBuilders.post("/reviews/me")
                        .header("X-USER-ID", "user123")
                        .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isConflict());
        Mockito.verify(reviewService, Mockito.times(1)).addReview("user123", review);
    }

    @Test
    void addReviewFailValidation() throws Exception {
        ReviewCreateRequest review = new ReviewCreateRequest(0, "", "", "", 0);
        mockMvc.perform(MockMvcRequestBuilders.post("/reviews/me")
                        .header("X-USER-ID", "user123")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException() instanceof ValidationFailedException));
    }

    @Test
    void editReview() throws Exception {
        long reviewId = 1L;
        ReviewUpdateRequest review = new ReviewUpdateRequest(5, "Updated review", "");
        Mockito.when(reviewService.editReview("user123", reviewId, review)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/reviews/me/" + reviewId)
                        .header("X-USER-ID", "user123")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(reviewService, Mockito.times(1)).editReview("user123", reviewId, review);
    }

    @Test
    void editReviewFailNotFound() throws Exception {
        long reviewId = 1L;
        ReviewUpdateRequest review = new ReviewUpdateRequest(5, "Updated review", "");
        Mockito.when(reviewService.editReview("user123", reviewId, review)).thenThrow(new ReviewNotFoundException(1L));

        mockMvc.perform(MockMvcRequestBuilders.put("/reviews/me/" + reviewId)
                        .header("X-USER-ID", "user123")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isNotFound());
        Mockito.verify(reviewService, Mockito.times(1)).editReview("user123", reviewId, review);
    }

    @Test
    void editReviewFailValidation() throws Exception {
        long reviewId = 1L;
        ReviewUpdateRequest review = new ReviewUpdateRequest(0, "", "");

        mockMvc.perform(MockMvcRequestBuilders.put("/reviews/me/" + reviewId)
                        .header("X-USER-ID", "user123")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException() instanceof ValidationFailedException));
    }

    @Test
    void getReview() throws Exception {
        long reviewId = 1L;
        Mockito.when(reviewService.getReview(reviewId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/" + reviewId)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(reviewService, Mockito.times(1)).getReview(reviewId);
    }


    @Test
    void getReviewByUserId() throws Exception {
        String userId = "user123";
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(reviewService.getReviewsByUserId(userId, pageable)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/user/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(reviewService, Mockito.times(1)).getReviewsByUserId(userId, pageable);
    }

    @Test
    void getReviewByBookId() throws Exception {
        long bookId = 1L;
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(reviewService.getReviewsByBookId(bookId, pageable)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/book/" + bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(reviewService, Mockito.times(1)).getReviewsByBookId(bookId, pageable);
    }
}
