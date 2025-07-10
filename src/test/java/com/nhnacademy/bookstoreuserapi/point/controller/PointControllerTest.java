package com.nhnacademy.bookstoreuserapi.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
@AutoConfigureMockMvc
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getPoints() throws Exception {
        ResponsePoint responsePoint1 = new ResponsePoint(1L, "userId123", 1L, 1L, LocalDateTime.now(), "500");
        ResponsePoint responsePoint2 = new ResponsePoint(2L, "userId123", 2L, 2L, LocalDateTime.now(), "300");

        List<ResponsePoint> pointList = List.of(responsePoint1, responsePoint2);
        Page<ResponsePoint> page = new PageImpl<>(pointList, PageRequest.of(0, 10), pointList.size());

        Mockito.when(pointService.findAll(eq("userId123"), any()))
                .thenReturn(page);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/me/point")
                        .header("X-USER-ID", "userId123"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("userId123"));
        Assertions.assertTrue(content.contains("500"));

        Mockito.verify(pointService, Mockito.times(1)).findAll(eq("userId123"), any());
    }

    @Test
    void getPointsFailBlank() throws Exception {
        ResponsePoint responsePoint1 = new ResponsePoint(1L, "userId123", 1L, 1L, LocalDateTime.now(), "500");
        ResponsePoint responsePoint2 = new ResponsePoint(2L, "userId123", 2L, 2L, LocalDateTime.now(), "300");

        List<ResponsePoint> pointList = List.of(responsePoint1, responsePoint2);
        Page<ResponsePoint> page = new PageImpl<>(pointList, PageRequest.of(0, 10), pointList.size());

        Mockito.when(pointService.findAll(eq("userId123"), any()))
                .thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/me/point")
                        .header("X-USER-ID", ""))
                .andExpect(status().is4xxClientError())
                .andReturn();


        Mockito.verify(pointService, Mockito.times(0)).findAll(eq("userId123"), any());
    }

    @Test
    void getPointsFailExceed20Letter() throws Exception {
        ResponsePoint responsePoint1 = new ResponsePoint(1L, "userId123", 1L, 1L, LocalDateTime.now(), "500");
        ResponsePoint responsePoint2 = new ResponsePoint(2L, "userId123", 2L, 2L, LocalDateTime.now(), "300");

        List<ResponsePoint> pointList = List.of(responsePoint1, responsePoint2);
        Page<ResponsePoint> page = new PageImpl<>(pointList, PageRequest.of(0, 10), pointList.size());

        Mockito.when(pointService.findAll(eq("userId123"), any()))
                .thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/me/point")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm".repeat(20)))
                .andExpect(status().is4xxClientError())
                .andReturn();


        Mockito.verify(pointService, Mockito.times(0)).findAll(eq("userId123"), any());
    }

    @Test
    void savePoint() throws Exception {
        PointCreateRequest request = new PointCreateRequest("test", 1L, 2L, LocalDateTime.of(2015, 11, 12, 5, 12, 12, 12), "500");
        ResponsePoint response = new ResponsePoint(1L, "test", 1L, 2L, LocalDateTime.of(2015, 11, 12, 5, 12, 12, 12), "500");

        Mockito.when(pointService.savePoint("test", request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/me/point")
                        .header("X-USER-ID", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(pointService, Mockito.times(1)).savePoint("test", request);
    }

    @Test
    void savePointValidationFail() throws Exception {
        PointCreateRequest request = new PointCreateRequest("", 0L, 0L, LocalDateTime.now(), "0");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/me/point")
                        .header("X-USER-ID", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result ->
                        Assertions.assertInstanceOf(ValidationFailedException.class, result.getResolvedException()));
    }
}
