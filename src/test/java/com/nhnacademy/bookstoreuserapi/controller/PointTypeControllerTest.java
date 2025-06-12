package com.nhnacademy.bookstoreuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.domain.request.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.service.PointTypeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointTypeController.class)
@AutoConfigureMockMvc
class PointTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PointTypeService pointTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("전체 포인트 타입 조회 성공")
    void getAllPointTypes() throws Exception {
        List<ResponsePointType> mockList = List.of(
                new ResponsePointType(1L, "신규가입", 100L, 5, "GOLD"),
                new ResponsePointType(2L, "리뷰작성", 200L, 10, "GOLD")
        );

        Mockito.when(pointTypeService.getAllPointTypes()).thenReturn(mockList);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/pointType"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("신규가입", "리뷰작성");
        Mockito.verify(pointTypeService, Mockito.times(1)).getAllPointTypes();
    }

    @Test
    @DisplayName("등급명으로 포인트 타입 조회 성공")
    void getPointTypeByGradeName() throws Exception {
        Mockito.when(pointTypeService.getPointTypeByGradeName("GOLD"))
                .thenReturn(List.of(new ResponsePointType(1L, "가입", 100L, 5, "GOLD")));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/pointType")
                        .param("gradeName", "GOLD"))
                .andExpect(status().isOk());

        Mockito.verify(pointTypeService).getPointTypeByGradeName("GOLD");
    }

    @Test
    @DisplayName("포인트 타입 생성 성공")
    void createPointType() throws Exception {
        PointTypeCreateRequest request = new PointTypeCreateRequest("순수 주문금액", 200L, 20, "BRONZE");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/pointType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(pointTypeService).savePointType(request);
    }

    @Test
    @DisplayName("포인트 타입 삭제 성공")
    void deletePointType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/pointType/{typeId}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(pointTypeService).deletePointType(1L);
    }

    @Test
    @DisplayName("earningPoint 업데이트 성공")
    void updateEarningPoint() throws Exception {
        ResponsePointType response = new ResponsePointType(1L, "주문", 999L, 5, "BRONZE");
        Mockito.when(pointTypeService.updateEarningPoint(999L, 1L)).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/users/pointType/{typeId}/point", 1L)
                        .param("point", "999"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("999");
        Mockito.verify(pointTypeService).updateEarningPoint(999L, 1L);
    }

    @Test
    @DisplayName("earningRate 업데이트 성공")
    void updateEarningRate() throws Exception {
        ResponsePointType response = new ResponsePointType(1L, "주문", 500L, 15, "SILVER");
        Mockito.when(pointTypeService.updateEarningRate(15, 1L)).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/users/pointType/{typeId}/rate", 1L)
                        .param("rate", "15"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("15");
        Mockito.verify(pointTypeService).updateEarningRate(15, 1L);
    }
}

