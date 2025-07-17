package com.nhnacademy.bookstoreuserapi.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.common.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.user.domain.*;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.service.UserService;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.BASIC;
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

    @MockBean
    PointTypeService pointTypeService;

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
    @DisplayName("OAuth2 회원 등록 성공")
    void registerOAuth2User_success() throws Exception {
        Oauth2UserCreateRequest request = new Oauth2UserCreateRequest(
                "PAYCO", "asdf-342rdw-adfv4-fae324253", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1)
        );

        mockMvc.perform(post("/users/register/oauth2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("OAuth2회원 등록 실패 - 유효성 검사")
    void registerOAuth2User_fail() throws Exception {
        Oauth2UserCreateRequest request = new Oauth2UserCreateRequest(
                "", "password", "홍길동", "01012345678",
                "asdf", LocalDate.of(1990, 1, 1));
        mockMvc.perform(post("/users/register/oauth2")
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
    @DisplayName("회원 단건 조회")
    void getUserByUserNo_success() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        user.setUserNo(1L);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);
        Mockito.when(userService.getUserByUserNo(1L)).thenReturn(new ResponseUser(user));

        mockMvc.perform(get("/users/user/1"))
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

    @Test
    @DisplayName("회원 조회 실패 - 20자 초과 사용자 ID")
    void getUserInfo_Exceed20Letter() throws Exception {
        mockMvc.perform(get("/users/me")
                        .header("X-USER-ID", "asfghjklqwertyuiopzxcvbnm".repeat(20)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 아이디 찾기")
    void getUserIdByUserNameAndUserEmail_success() throws Exception {
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);
        Mockito.when(userService.getUserIdByUserNameAndUserEmail("홍길동","hong@test.com")).thenReturn(new ResponseUserId(user));

        mockMvc.perform(get("/users/findId")
                        .param("userName", "홍길동")
                        .param("userEmail", "hong@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"));
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteUser_success() throws Exception {
        mockMvc.perform(put("/users/me/status/WITHDRAWN")
                        .header("X-USER-ID", "user123"))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("회원 삭제 - 실패 - 빈 사용자 ID")
    void deleteUser_fail_blank() throws Exception {
        mockMvc.perform(put("/users/me/status/WITHDRAWN")
                        .header("X-USER-ID", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 삭제 - 실패 - 255자 초과 사용자 ID")
    void deleteUser_fail_exceed255Letter() throws Exception {
        mockMvc.perform(put("/users/me/status/WITHDRAWN")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm".repeat(20)))
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

    @Test
    @DisplayName("개인정보 수정 실패 - 유효성 검사")
    void updatePersonalInfo_fail() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(
                "newPassword", "이수정", "01011112222",
                "asfd", LocalDate.of(1995, 5, 5));
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

    @Test
    @DisplayName("회원 정보 수정 - 실패 - 255자 초과 사용자 ID")
    void updatePersonalInfo_fail_exceed255Letter() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(
                "newPassword", "이수정", "01011112222",
                "test@test", LocalDate.of(1995, 5, 5));
        mockMvc.perform(put("/users/me/personalinformation")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm".repeat(20))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("개인정보 수정-no xuserId")
    void updatePersonalInfoPathVariable() throws Exception {
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

        mockMvc.perform(put("/users/user123/personalinformation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("이수정"));
    }

    @Test
    @DisplayName("개인정보 수정 실패 - 유효성 검사 - no xuserId")
    void updatePersonalInfoPathVariable_fail() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest(
                "newPassword", "이수정", "01011112222",
                "asfd", LocalDate.of(1995, 5, 5));
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
        Mockito.when(userService.getUser("user123")).thenReturn(new ResponseUser(user));

        mockMvc.perform(put("/users/me/lastloginat")
                        .header("X-USER-ID", "user123"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신 - 실패 - 빈 사용자 ID")
    void updateLastLoginAtFailBlank() throws Exception {
        mockMvc.perform(put("/users/me/lastloginat")
                        .header("X-USER-ID", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신 - 실패 - 255자 초과 사용자 ID")
    void updateLastLoginAtFailExceed255Letter() throws Exception {
        mockMvc.perform(put("/users/me/lastloginat")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm".repeat(20)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("포인트 증가")
    void plusPoint_success() throws Exception {

        Long userNo = 1L;
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));
        user.setUserNo(userNo);
        user.setUserPoint(2000);
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);

        Mockito.when(userService.plusPoint(eq(user.getUserNo()), any(Integer.class)))
                .thenReturn(new ResponseUser(user));

        mockMvc.perform(put("/users/{userNo}/plus-point", userNo)
                        .param("point", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userPoint").value(2000));
    }

    @Test
    @DisplayName("포인트 증가 실패 - 잘못된 사용자 ID (non-numeric)")
    void plusPoint_fail_invalidUserNo() throws Exception {
        mockMvc.perform(put("/users/non-numeric/plus-point")
                        .param("point", "1000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("포인트 감소")
    void minusPoint_success() throws Exception {
        Long userNo = 1L;
        User user = new User("user123", "pw", "홍길동", "01012345678",
                "hong@test.com", LocalDate.of(1990, 1, 1));

        user.setUserNo(userNo);
        user.setUserPoint(-500);
        user.setUserStatus(User.Status.ACTIVE);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);

        Mockito.when(userService.minusPoint(eq(user.getUserNo()), any(Integer.class)))
                .thenReturn(new ResponseUser(user));

        mockMvc.perform(put("/users/{userNo}/minus-point",  userNo)
                        .param("point", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userPoint").value(-500));
    }

    @Test
    @DisplayName("포인트 감소 실패 - 잘못된 사용자 ID (non-numeric)")
    void minusPoint_fail_invalidUserNo() throws Exception {
        mockMvc.perform(put("/users/me/minus-point")
                        .param("point", "500"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("내 포인트 조회")
    void getMyPoint_success() throws Exception {
        Mockito.when(userService.getUserPoint("user123")).thenReturn(2000);

        mockMvc.perform(get("/users/me/my-point")
                        .header("X-USER-ID", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2000));
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
        mockMvc.perform(put("/users/me/status")
                        .param("status", "WITHDRAWN")
                        .header("X-USER-ID", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상태 변경 - 실패 - 255자 초과 사용자 ID")
    void updateStatusFailExceed255Letter() throws Exception {
        mockMvc.perform(put("/users/me/status")
                        .param("status", "WITHDRAWN")
                        .header("X-USER-ID", "asdfghjklqwertyuiopzxcvbnm".repeat(20)))
                .andExpect(status().isBadRequest());
    }

}
