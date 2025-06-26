package com.nhnacademy.bookstoreuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUser;
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

import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.BASIC;
import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.ROYAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        Mockito.when(userService.getUser("user123")).thenReturn(new ResponseUser(user));

        mockMvc.perform(get("/users/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.userName").value("홍길동"));
    }

    @Test
    @DisplayName("회원 정보 조회")
    void getUserInfo_success() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);
        Mockito.when(userService.getUser("user123")).thenReturn(new ResponseUser(user));

        mockMvc.perform(get("/users/me")
                        .header("X-USER-ID", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.userName").value("홍길동"));
    }

    @Test
    @DisplayName("회원 조회 실패 - 없는 사용자")
    void getUser_notFound() throws Exception {
        Mockito.when(userService.getUser("notExist")).thenThrow(new UserNotFoundException("notExist"));

        mockMvc.perform(get("/users/notExist"))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("회원 조회 실패 - 없는 사용자")
    void getUserInfo_notFound() throws Exception {
        Mockito.when(userService.getUser("notExist")).thenThrow(new UserNotFoundException("notExist"));

        mockMvc.perform(get("/users/me")
                        .header("X-USER-ID", "notExist"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 조회 실패 - 빈 사용자 ID")
    void getUserInfo_Blank() throws Exception {
        mockMvc.perform(get("/users/me")
                        .header("X-USER-ID", ""))
                .andExpect(status().isBadRequest());
    }

//    @Test
    @DisplayName("회원 조회 실패 - 20자 초과 사용자 ID")
    void getUserInfo_Exceed20Letter() throws Exception {
        mockMvc.perform(get("/users/me")
                        .header("X-USER-ID", "asfghjklqwertyuiopzxcvbnm"))
                .andExpect(status().isBadRequest());
    }

//    @Test
    @DisplayName("회원 삭제")
    void deleteUser_success() throws Exception {
        mockMvc.perform(delete("/users")
                        .header("X-USER-ID", "user123"))
                .andExpect(status().isOk());
    }
//    @Test
    @DisplayName("회원 삭제 - 실패 - 빈 사용자 ID")
    void deleteUser_fail_blank() throws Exception {
        mockMvc.perform(delete("/users")
                        .header("X-USER-ID", ""))
                .andExpect(status().isBadRequest());
    }

//    @Test
    @DisplayName("회원 삭제 - 실패 - 20자 초과 사용자 ID")
    void deleteUser_fail_exceed20Letter() throws Exception {
        mockMvc.perform(delete("/users")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("개인정보 수정")
    void updatePersonalInfo() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(
                "newPassword", "이수정", "01011112222",
                "lee@test.com", LocalDate.of(1995, 5, 5)
        );
        User updatedUser = new User("user123", "newPassword", "이수정", "01011112222",
                "lee@test.com", LocalDate.of(1995, 5, 5));

        updatedUser.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        updatedUser.setUserGrade(userGrade);

        Mockito.when(userService.updatePersonalInformation(eq("user123"), any(UserUpdateRequest.class)))
                .thenReturn(new ResponseUser(updatedUser));

        mockMvc.perform(put("/users/me/personalinformation")
                        .header("X-USER-ID", "user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("이수정"));
    }

//    @Test
    @DisplayName("개인정보 수정 실패 - 유효성 검사")
    void updatePersonalInfo_fail() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(
                "newPassword", "이수정", "01011112222",
                "", LocalDate.of(1995, 5, 5));
        mockMvc.perform(put("/users/me/personalinformation")
                        .header("X-USER-ID", "user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException()).isInstanceOf(ValidationFailedException.class));
    }

    @Test
    @DisplayName("회원 정보 수정 - 실패 - 없는 사용자")
    void updatePersonalInfo_fail_blank() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(
                 "newPassword", "이수정", "01011112222",
                "test@test", LocalDate.of(1995, 5, 5));
        mockMvc.perform(put("/users/me/personalinformation")
                        .header("X-USER-ID", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

//    @Test
    @DisplayName("회원 정보 수정 - 실패 - 20자 초과 사용자 ID")
    void updatePersonalInfo_fail_exceed20Letter() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(
                 "newPassword", "이수정", "01011112222",
                "test@test", LocalDate.of(1995, 5, 5));
        mockMvc.perform(put("/users/me/personalinformation")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신")
    void updateLastLoginAt() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);
        Mockito.when(userService.getUser("user123")).thenReturn(new ResponseUser(user));

        mockMvc.perform(put("/users/me/lastloginat")
                        .header("X-USER-ID", "user123"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신 - 실패 - 빈 사용자 ID")
    void updateLastLoginAtFailBlank() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);

        mockMvc.perform(put("/users/me/lastloginat")
                        .header("X-USER-ID", ""))
                .andExpect(status().isBadRequest());
    }

//    @Test
    @DisplayName("마지막 로그인 시간 갱신 - 실패 - 20자 초과 사용자 ID")
    void updateLastLoginAtFailExceed20Letter() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);

        mockMvc.perform(put("/users/me/lastloginat")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm"))
                .andExpect(status().isBadRequest());
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

        Mockito.when(userService.updatePoint(eq("user123"), any(Integer.class)))
                .thenReturn(new ResponseUser(user));


        mockMvc.perform(put("/users/me/point")
                        .param("point", "2000")
                .header("X-USER-ID", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userPoint").value(2000));
    }

    @Test
    @DisplayName("포인트 수정- 실패 - 빈 사용자 ID")
    void updatePointFailBlank() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserPoint(2000);
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);

        mockMvc.perform(put("/users/me/point")
                        .param("point", "2000")
                        .header("X-USER-ID", ""))
                .andExpect(status().isBadRequest());
    }

//    @Test
    @DisplayName("포인트 수정- 실패 - 20자 초과 사용자 ID")
    void updatePointFailExceed20Letter() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserPoint(2000);
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);

        mockMvc.perform(put("/users/me/point")
                        .param("point", "2000")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상태 변경")
    void updateStatus() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.WITHDRAWN);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);
        Mockito.when(userService.updateUserStatus(eq("user123"), any(User.Status.class))).thenReturn(new ResponseUser(user));

        mockMvc.perform(put("/users/me/status")
                        .param("status", "WITHDRAWN")
                        .header("X-USER-ID", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus").value("WITHDRAWN"));
    }

    @Test
    @DisplayName("상태 변경 - 실패 - 빈 사용자 ID")
    void updateStatusFailBlank() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.WITHDRAWN);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);


        mockMvc.perform(put("/users/me/status")
                        .param("status", "WITHDRAWN")
                        .header("X-USER-ID", ""))
                .andExpect(status().isBadRequest());
    }

//    @Test
    @DisplayName("상태 변경 - 실패 - 20자 초과 사용자 ID")
    void updateStatusFailExceed20Letter() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.WITHDRAWN);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);

        mockMvc.perform(put("/users/me/status")
                        .param("status", "WITHDRAWN")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 등급 수정")
    void updateUserGrade() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        user.setUserGrade(new UserGrade(ROYAL, 100000L));

        Mockito.when(userService.updateUserGradeName("user123", ROYAL.toString()))
                .thenReturn(new ResponseUser(user));

        mockMvc.perform(put("/users/me/grade")
                        .param("gradeName", "ROYAL")
                        .header("X-USER-ID", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userGradeName").value("ROYAL"));
    }

    @Test
    @DisplayName("회원 등급 수정 - 실패 - 빈 사용자 ID")
    void updateUserGradeFailBlank() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        user.setUserGrade(new UserGrade(BASIC, 0L));

        mockMvc.perform(put("/users/me/grade")
                        .param("gradeName", "ROYAL")
                        .header("X-USER-ID", ""))
                .andExpect(status().isBadRequest());
    }

//    @Test
    @DisplayName("회원 등급 수정 - 실패 - 20자 초과 사용자 ID")
    void updateUserGradeFailExceed20Letter() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        user.setUserGrade(new UserGrade(BASIC, 0L));

        mockMvc.perform(put("/users/me/grade")
                        .param("gradeName", "ROYAL")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm"))
                .andExpect(status().isBadRequest());
    }
}

