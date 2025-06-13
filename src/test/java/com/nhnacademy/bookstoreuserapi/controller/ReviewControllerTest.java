package com.nhnacademy.bookstoreuserapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestReview;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestReview;
import com.nhnacademy.bookstoreuserapi.exception.InvalidDataException;
import com.nhnacademy.bookstoreuserapi.exception.ReviewAlreadyExistsBookException;
import com.nhnacademy.bookstoreuserapi.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreuserapi.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewServiceImpl reviewService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addReview() throws Exception {
        SignUpRequestReview review = new SignUpRequestReview(5, "Great book!", "", "user123", 1L);
        Mockito.when(reviewService.addReview(review)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/reviews")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(reviewService, Mockito.times(1)).addReview(review);
    }

    @Test
    void addReviewFailAlreadyExist() throws Exception {
        SignUpRequestReview review = new SignUpRequestReview(5, "Great book!", "", "user123", 1L);
        Mockito.when(reviewService.addReview(review)).thenThrow(new ReviewAlreadyExistsBookException("user123", 1L));

        mockMvc.perform(MockMvcRequestBuilders.post("/reviews")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isConflict());
        Mockito.verify(reviewService, Mockito.times(1)).addReview(review);
    }

    @Test
    void addReviewFailInvalidReviewData() throws Exception {
        SignUpRequestReview review = new SignUpRequestReview(0, "", "", "user123", 1L); // Invalid data
        Mockito.when(reviewService.addReview(review)).thenThrow(new InvalidDataException("Invalid review data"));

        mockMvc.perform(MockMvcRequestBuilders.post("/reviews")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isBadRequest());
        Mockito.verify(reviewService, Mockito.times(1)).addReview(review);
    }

    @Test
    void editReview() throws Exception {
        long reviewId = 1L;
        EditRequestReview review = new EditRequestReview(5, "Updated review", "");
        Mockito.when(reviewService.editReview(reviewId, review)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/reviews/" + reviewId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(reviewService, Mockito.times(1)).editReview(reviewId, review);
    }

    @Test
    void editReviewFailNotFound() throws Exception {
        long reviewId = 1L;
        EditRequestReview review = new EditRequestReview(5, "Updated review", "");
        Mockito.when(reviewService.editReview(reviewId, review)).thenThrow(new ReviewNotFoundException(1L));

        mockMvc.perform(MockMvcRequestBuilders.put("/reviews/" + reviewId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isNotFound());
        Mockito.verify(reviewService, Mockito.times(1)).editReview(reviewId, review);
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
    void getReviewFailInvalidData() throws Exception {
        long reviewId = -1L;
        Mockito.when(reviewService.getReview(reviewId)).thenThrow(new InvalidDataException("Invalid review ID"));

        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/" + reviewId)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        Mockito.verify(reviewService, Mockito.times(1)).getReview(reviewId);
    }

    @Test
    void getReviewByUserId() throws Exception {
        String userId = "user123";
        Mockito.when(reviewService.getReviewsByUserId(userId)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/user/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(reviewService, Mockito.times(1)).getReviewsByUserId(userId);
    }

    @Test
    void getReviewByBookId() throws Exception {
        long bookId = 1L;
        Mockito.when(reviewService.getReviewsByBookId(bookId)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/book/" + bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(reviewService, Mockito.times(1)).getReviewsByBookId(bookId);
    }
}
