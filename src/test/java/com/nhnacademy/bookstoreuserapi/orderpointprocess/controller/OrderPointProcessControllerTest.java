package com.nhnacademy.bookstoreuserapi.orderpointprocess.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.orderpointprocess.dto.PointType;
import com.nhnacademy.bookstoreuserapi.orderpointprocess.dto.request.OrderPointMinusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderpointprocess.dto.request.OrderPointPlusProcessRequest;
import com.nhnacademy.bookstoreuserapi.orderpointprocess.service.OrderPointProcessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderPointProcessController.class)
class OrderPointProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderPointProcessService orderPointProcessService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 포인트 적립 성공")
    void testOrderPointPlus() throws Exception {
        Long userNo = 1L;
        OrderPointPlusProcessRequest request = new OrderPointPlusProcessRequest(100L, 500, PointType.ORDER);

        doNothing().when(orderPointProcessService).orderPointPlusProcess(userNo, request);

        mockMvc.perform(post("/users/order-point/{userNo}/plus", userNo)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 포인트 적립 실패 - 잘못된 요청")
    void testOrderPointPlus_withInvalidRequest() throws Exception {
        Long userNo = 1L;
        OrderPointPlusProcessRequest request = new OrderPointPlusProcessRequest(null, 500, PointType.ORDER);

        mockMvc.perform(post("/users/order-point/{userNo}/plus", userNo)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문 포인트 사용 성공")
    void testOrderPointMinus() throws Exception {
        Long userNo = 1L;
        OrderPointMinusProcessRequest request = new OrderPointMinusProcessRequest(200L, 300, PointType.ORDER);

        doNothing().when(orderPointProcessService).orderPointMinusProcess(userNo, request);

        mockMvc.perform(post("/users/order-point/{userNo}/minus", userNo)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문 포인트 사용 실패 - 잘못된 요청")
    void testOrderPointMinus_withInvalidRequest() throws Exception {
        Long userNo = 1L;
        OrderPointMinusProcessRequest request = new OrderPointMinusProcessRequest(null, 300, PointType.ORDER);

        mockMvc.perform(post("/users/order-point/{userNo}/minus", userNo)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}