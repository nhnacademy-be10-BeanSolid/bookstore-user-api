package com.nhnacademy.bookstoreuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.domain.request.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.service.PointService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

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
        ResponsePoint responsePoint1 = new ResponsePoint(1L, "userId123", 1L, 1L, LocalDateTime.now(), 500L);
        ResponsePoint responsePoint2 = new ResponsePoint(2L, "userId123", 2L, 2L, LocalDateTime.now(), 300L);

        Mockito.when(pointService.findAll("userId123"))
                .thenReturn(List.of(responsePoint1, responsePoint2));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/point/{userId}", "userId123"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("userId123"));
        Assertions.assertTrue(content.contains("500"));

        Mockito.verify(pointService, Mockito.times(1)).findAll("userId123");
    }

    @Test
    void savePoint() throws Exception {
        PointCreateRequest request = new PointCreateRequest("test", 1L, 2L, LocalDateTime.of(2015, 11, 12, 5, 12, 12, 12), 500L);
        ResponsePoint response = new ResponsePoint(1L, "test", 1L, 2L, LocalDateTime.of(2015, 11, 12, 5, 12, 12, 12), 500L);

        Mockito.when(pointService.savePoint(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/point")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());

        Mockito.verify(pointService, Mockito.times(1)).savePoint(request);
    }
}
