package com.nhnacademy.bookstoreuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.domain.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.GuestUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseGuest;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.GuestService;
import org.junit.jupiter.api.DisplayName;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GuestController.class)
@AutoConfigureMockMvc
class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestService guestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게스트 등록 성공")
    void registerGuest_Success() throws Exception {
        GuestCreateRequest request = new GuestCreateRequest(
                "password123",
                "Guest User",
                "01012345678",
                "Seoul, Korea",
                "guest@example.com"
        );

        ResponseGuest response = new ResponseGuest(
                "password123",
                "Guest User",
                "01012345678",
                "Seoul, Korea",
                "guest@example.com"
        );

        Mockito.when(guestService.addGuest(Mockito.any(GuestCreateRequest.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/guests/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains(
                "password123",
                "Guest User",
                "01012345678",
                "Seoul, Korea",
                "guest@example.com"
        );

        Mockito.verify(guestService).addGuest(Mockito.any(GuestCreateRequest.class));
    }

    @Test
    @DisplayName("게스트 등록 실패 - 유효성 검사 실패")
    void registerGuest_ValidationFail() throws Exception {
        GuestCreateRequest request = new GuestCreateRequest(
                "", "", "", "", "invalid-email"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/guests/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ValidationFailedException.class));
    }

    @Test
    @DisplayName("게스트 조회 성공")
    void getGuest_Success() throws Exception {
        String email = "guest@example.com";

        ResponseGuest response = new ResponseGuest(
                "password123",
                "Guest User",
                "01012345678",
                "Seoul, Korea",
                email
        );

        Mockito.when(guestService.getGuest(email)).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/guests/{guestEmail}", email))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains(
                "password123",
                "Guest User",
                "01012345678",
                "Seoul, Korea",
                email
        );

        Mockito.verify(guestService).getGuest(email);
    }

    @Test
    @DisplayName("게스트 삭제 성공")
    void deleteGuest_Success() throws Exception {
        String email = "guest@example.com";

        mockMvc.perform(MockMvcRequestBuilders.delete("/guests/{guestEmail}", email))
                .andExpect(status().isOk());

        Mockito.verify(guestService).deleteGuest(email);
    }

    @Test
    @DisplayName("게스트 수정 성공")
    void updateGuest_Success() throws Exception {
        String email = "guest@example.com";

        GuestUpdateRequest updateRequest = new GuestUpdateRequest(
                "newpassword",
                "Updated User",
                "01098765432",
                "Busan, Korea"
        );

        ResponseGuest response = new ResponseGuest(
                "newpassword",
                "Updated User",
                "01098765432",
                "Busan, Korea",
                email
        );

        Mockito.when(guestService.updateGuest(Mockito.eq(email), Mockito.any(GuestUpdateRequest.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/guests/{guestEmail}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains(
                "newpassword",
                "Updated User",
                "01098765432",
                "Busan, Korea",
                email
        );

        Mockito.verify(guestService).updateGuest(Mockito.eq(email), Mockito.any(GuestUpdateRequest.class));
    }

    @Test
    @DisplayName("게스트 수정 실패 - 유효성 검사 실패")
    void updateGuest_ValidationFail() throws Exception {
        String email = "guest@example.com";

        GuestUpdateRequest updateRequest = new GuestUpdateRequest(
                "", "", "", ""
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/guests/{guestEmail}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ValidationFailedException.class));
    }
}
