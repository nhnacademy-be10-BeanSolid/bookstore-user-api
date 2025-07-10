package com.nhnacademy.bookstoreuserapi.user.service;

import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.domain.Oauth2UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.UserCreateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.UserUpdateRequest;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUser;
import com.nhnacademy.bookstoreuserapi.user.domain.ResponseUserId;
import com.nhnacademy.bookstoreuserapi.user.exception.UserAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.usergrade.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDate;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.ROYAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Sql(scripts = "/user-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private final String userId = "test";

    @BeforeEach
    void setup() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    @DisplayName("사용자 저장 성공")
    void saveUser_success() {
        UserCreateRequest request = new UserCreateRequest(
                "newUser",
                "plainPassword",
                "김철수",
                "01098765432",
                "kim@test.com",
                LocalDate.of(1995, 6, 15)
        );

        userService.saveUser(request);

        User savedUser = userRepository.findByUserId("newUser");
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserPassword()).isEqualTo("encodedPassword");
    }

    @Test
    @DisplayName("Oauth2사용자 저장 성공")
    void saveOauth2User_success() {
        Oauth2UserCreateRequest request = new Oauth2UserCreateRequest(
                "PAYCO",
                "s8f7a-f9sd98-f8s9d73",
                "김철수",
                "01098765432",
                "kim@test.com",
                LocalDate.of(1995, 6, 15)
        );

        userService.saveOauth2User(request);

        User savedUser = userRepository.findByUserId("PAYCOs8f7a-f9sd98-f8s9d73");
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getProviderId()).isEqualTo("s8f7a-f9sd98-f8s9d73");
    }

    @Test
    @DisplayName("중복 사용자 저장 실패")
    void saveUser_alreadyExists() {

        UserCreateRequest request = new UserCreateRequest(
                userId,
                "testPassword",
                "test",
                "01098765432",
                "test@test.com",
                LocalDate.of(1995, 6, 15)
        );

        assertThatThrownBy(() -> userService.saveUser(request))
                .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    @DisplayName("사용자 조회 성공")
    void findByUserId_success() {
        ResponseUser user = userService.getUser(userId);
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회 실패")
    void findByUserId_notFound() {
        assertThatThrownBy(() -> userService.getUser("notExists"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("사용자 조회 성공 - UserNo")
    void findByUserNo_success() {
        ResponseUser user = userService.getUserByUserNo(1L);
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회 실패 - UserNo")
    void findByUserNo_notFound() {
        assertThatThrownBy(() -> userService.getUserByUserNo(0L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("사용자 아이디 찾기 - 이름과 이메일을 이용")
    void findByUserNameAndUserEmail_success() {
        ResponseUserId user = userService.getUserIdByUserNameAndUserEmail("test", "test@test.com");
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 아이디 찾기")
    void findByUserNameAndUserEmail_notFound() {
        assertThatThrownBy(() -> userService.getUserIdByUserNameAndUserEmail("test1", "test@test.com"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("개인정보 수정")
    void updatePersonalInformation() {
        UserUpdateRequest request = new UserUpdateRequest(
                "newPassword",
                "수정된이름",
                "01012345678",
                "updated@test.com",
                LocalDate.of(1993, 12, 12)
        );

        ResponseUser updatedUser = userService.updatePersonalInformation(userId, request);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUserName()).isEqualTo("수정된이름");
        assertThat(updatedUser.getUserEmail()).isEqualTo("updated@test.com");
    }


    @Test
    @DisplayName("개인정보 수정 실패 - 존재하지 않는 사용자")
    void updatePersonalInformation_notFound() {
        UserUpdateRequest request = new UserUpdateRequest(
                "password",
                "수정된이름",
                "01000000000",
                "fail@test.com",
                LocalDate.of(1990, 1, 1)
        );

        assertThatThrownBy(() -> userService.updatePersonalInformation("notExists", request))
                .isInstanceOf(UserNotFoundException.class);
    }


    @Test
    @DisplayName("로그인 시간 업데이트")
    void updateLastLoginAt() {
        userService.updateLastLoginAt(userId);

        User updated = userRepository.findByUserId(userId);
        assertThat(updated.getLastLoginAt()).isNotNull();
    }

    @Test
    @DisplayName("로그인 시간 업데이트 실패 - 존재하지 않는 사용자")
    void updateLastLoginAt_notFound() {
        assertThatThrownBy(() -> userService.updateLastLoginAt("notExists"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("포인트 업데이트")
    void updatePoint() {
        userService.updatePoint(userId, 1000);

        User updated = userRepository.findByUserId(userId);
        assertThat(updated.getUserPoint()).isEqualTo(1500);
    }

    @Test
    @DisplayName("포인트 업데이트 실패 - 존재하지 않는 사용자")
    void updatePoint_notFound() {
        assertThatThrownBy(() -> userService.updatePoint("notExists", 1000))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("회원 등급 업데이트")
    void updateGrade() {
        userService.updateUserGradeName(userId);
        User updated = userRepository.findByUserId(userId);
        assertThat(updated.getUserGrade().getGradeName()).isEqualTo(ROYAL);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 등급 업데이트 실패")
    void updateGrade_notFound() {
        assertThatThrownBy(() -> userService.updateUserGradeName("unknown"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 등급 업데이트 실패 - 잘못된 등급")
    void updateGrade_invalidGrade() {
        assertThatThrownBy(() -> userService.updateUserGradeName(userId))
                .isInstanceOf(UserGradeNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 등급 업데이트 실패 - 저장되지 않은 등급")
    void updateGrade_notSavedGrade() {
        assertThatThrownBy(() -> userService.updateUserGradeName(userId))
                .isInstanceOf(UserGradeNotFoundException.class);
    }

    @Test
    @DisplayName("상태 업데이트")
    void updateUserStatus() {
        userService.updateUserStatus(userId, User.Status.WITHDRAWN);

        User updated = userRepository.findByUserId(userId);
        assertThat(updated.getUserStatus()).isEqualTo(User.Status.WITHDRAWN);
    }

    @Test
    @DisplayName("상태 업데이트 실패 - 존재하지 않는 사용자")
    void updateUserStatus_notFound() {
        assertThatThrownBy(() -> userService.updateUserStatus("unknown", User.Status.WITHDRAWN))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("사용자 삭제")
    void deleteUser() {
        userService.deleteUser(userId);
        User updated = userRepository.findByUserId(userId);
        assertThat(updated.getUserStatus()).isEqualTo(User.Status.WITHDRAWN);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 삭제 실패")
    void deleteUser_notFound() {
        assertThatThrownBy(() -> userService.deleteUser("unknown"))
                .isInstanceOf(UserNotFoundException.class);
    }
}
