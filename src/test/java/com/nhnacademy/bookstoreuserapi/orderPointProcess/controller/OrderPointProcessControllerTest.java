package com.nhnacademy.bookstoreuserapi.orderPointProcess.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.domain.request.OrderPointPlusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderPointProcess.service.OrderPointProcessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderPointProcessController.class)
class OrderPointProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderPointProcessService orderPointProcessService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("포인트 적립 성공")
    @Test
    void orderPointPlus_success() throws Exception {
        OrderPointPlusProcessRequest request = new OrderPointPlusProcessRequest(1L, 100);

        mockMvc.perform(post("/users/order-point/{userId}/plus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(orderPointProcessService)
                .orderPointPlusProcess(Mockito.eq(1L), Mockito.any(OrderPointPlusProcessRequest.class));
    }

    @DisplayName("포인트 차감 성공")
    @Test
    void orderPointMinus_success() throws Exception {
        OrderPointMinusProcessRequest request = new OrderPointMinusProcessRequest(1L, 100);

        mockMvc.perform(post("/users/order-point/{userId}/minus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(orderPointProcessService)
                .orderPointMinusProcess(Mockito.eq(1L), Mockito.any(OrderPointMinusProcessRequest.class));
    }

    @DisplayName("포인트 적립 Validation 실패")
    @Test
    void orderPointPlus_validationFail() throws Exception {
        OrderPointPlusProcessRequest request = new OrderPointPlusProcessRequest(1L, -100);

        mockMvc.perform(post("/users/order-point/{userId}/plus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("포인트 차감 Validation 실패")
    @Test
    void orderPointMinus_validationFail() throws Exception {
        OrderPointMinusProcessRequest request = new OrderPointMinusProcessRequest(1L, -100);

        mockMvc.perform(post("/users/order-point/{userId}/minus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

