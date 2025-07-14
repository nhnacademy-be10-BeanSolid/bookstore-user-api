package com.nhnacademy.bookstoreuserapi.guest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.guest.dto.request.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.dto.response.ResponseGuest;
import com.nhnacademy.bookstoreuserapi.guest.service.GuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GuestController.class)
class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestService guestService;

    @Autowired
    private ObjectMapper objectMapper;

    private GuestCreateRequest guestCreateRequest;
    private ResponseGuest responseGuest;
    private Long orderId;
    private String encodedPassword;

    @BeforeEach
    void setUp() {
        orderId = 1L;
        String rawPassword = "testPassword123!";
        encodedPassword = "encodedTestPassword123!";
        guestCreateRequest = new GuestCreateRequest(rawPassword, orderId);
        responseGuest = new ResponseGuest(1L, orderId);
    }

    @Test
    void register_success() throws Exception {
        when(guestService.addGuest(any(GuestCreateRequest.class))).thenReturn(responseGuest);

        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guestCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/guests/" + orderId))
                .andExpect(jsonPath("$.guestId").value(responseGuest.getGuestId()))
                .andExpect(jsonPath("$.orderId").value(responseGuest.getOrderId()));

        verify(guestService, times(1)).addGuest(any(GuestCreateRequest.class));
    }

    @Test
    void register_validationFailed() throws Exception {
        GuestCreateRequest invalidRequest = new GuestCreateRequest("", null);

        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(ValidationFailedException.class, result.getResolvedException()));

        verify(guestService, never()).addGuest(any(GuestCreateRequest.class));
    }

    @Test
    void getGuest_success() throws Exception {
        when(guestService.getGuest(orderId)).thenReturn(responseGuest);

        mockMvc.perform(get("/guests/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestId").value(responseGuest.getGuestId()))
                .andExpect(jsonPath("$.orderId").value(responseGuest.getOrderId()));

        verify(guestService, times(1)).getGuest(orderId);
    }

    @Test
    void deleteGuest_success() throws Exception {
        doNothing().when(guestService).deleteGuest(orderId);

        mockMvc.perform(delete("/guests/{orderId}", orderId))
                .andExpect(status().isNoContent());

        verify(guestService, times(1)).deleteGuest(orderId);
    }

    @Test
    void getGuestPassword_success() throws Exception {
        when(guestService.getGuestEncodedPassword(orderId)).thenReturn(encodedPassword);

        mockMvc.perform(get("/guests/{orderId}/password", orderId))
                .andExpect(status().isOk())
                .andExpect(content().string(encodedPassword));

        verify(guestService, times(1)).getGuestEncodedPassword(orderId);
    }
}