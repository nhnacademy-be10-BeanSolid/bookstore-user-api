package com.nhnacademy.bookstoreuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.service.UserService;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.request.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 등록 성공")
    void registerUser_success() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "user123", "password", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1)
        );

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 단건 조회")
    void getUser_success() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        Mockito.when(userService.findById("user123")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.userName").value("홍길동"));
    }

    @Test
    @DisplayName("회원 조회 실패 - 없는 사용자")
    void getUser_notFound() throws Exception {
        Mockito.when(userService.findById("notExist")).thenThrow(new UserNotFoundException("notExist"));

        mockMvc.perform(get("/users/notExist"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteUser_success() throws Exception {
        mockMvc.perform(delete("/users/user123"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("개인정보 수정")
    void updatePersonalInfo() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(
                "user123", "newPassword", "이수정", "01011112222",
                "lee@test.com", LocalDate.of(1995, 5, 5)
        );

        User updatedUser = new User("user123", "newPassword", "김철수", "01011112222",
                "lee@test.com", LocalDate.of(1995, 5, 5));

        Mockito.when(userService.findById("user123")).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/users/user123/personalinformation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("이수정"));
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신")
    void updateLastLoginAt() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        Mockito.when(userService.findById("user123")).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/user123/lastloginat"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포인트 수정")
    void updatePoint() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserPoint(2000);
        Mockito.when(userService.findById("user123")).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/user123")
                        .param("point", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userPoint").value(2000));
    }

    @Test
    @DisplayName("상태 변경")
    void updateStatus() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.WITHDRAWN);
        Mockito.when(userService.findById("user123")).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/user123/status")
                        .param("status", "WITHDRAWN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus").value("WITHDRAWN"));
    }
}

