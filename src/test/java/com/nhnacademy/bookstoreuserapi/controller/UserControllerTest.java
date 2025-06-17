package com.nhnacademy.bookstoreuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.BASIC;
import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.ROYAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
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
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원 등록 실패 - 유효성 검사")
    void registerUser_fail() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "", "password", "홍길동", "01012345678",
                "asdf", LocalDate.of(1990, 1, 1));
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException()).isInstanceOf(ValidationFailedException.class));
    }

    @Test
    @DisplayName("회원 단건 조회")
    void getUser_success() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);
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
                .andExpect(status().isNoContent());
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

        updatedUser.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        updatedUser.setUserGrade(userGrade);

        Mockito.when(userService.findById("user123")).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/users/user123/personalinformation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("이수정"));
    }

    @Test
    @DisplayName("개인정보 수정 실패 - 유효성 검사")
    void updatePersonalInfo_fail() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(
                "", "newPassword", "이수정", "01011112222",
                "", LocalDate.of(1995, 5, 5));
        mockMvc.perform(put("/users/user123/personalinformation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException()).isInstanceOf(ValidationFailedException.class));
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신")
    void updateLastLoginAt() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);
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
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);
        Mockito.when(userService.findById("user123")).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/user123/point")
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
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);
        Mockito.when(userService.findById("user123")).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/user123/status")
                        .param("status", "WITHDRAWN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus").value("WITHDRAWN"));
    }


    @Test
    @DisplayName("회원 등급 수정")
    void updateUserGrade() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        user.setUserGrade(new UserGrade(BASIC, 0L));

        Mockito.doAnswer(invocation -> {
            user.setUserGrade(new UserGrade(ROYAL, 100000L));
            return null;
        }).when(userService).updateUserGradeName("user123", "ROYAL");

        Mockito.when(userService.findById("user123")).thenReturn(Optional.of(user));

        mockMvc.perform(put("/users/user123/grade")
                        .param("gradeName", "ROYAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userGradeName").value("ROYAL"));
    }
}

