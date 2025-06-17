package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.exception.UserAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreuserapi.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.UserGradeRepository;
import com.nhnacademy.bookstoreuserapi.repository.UserRepository;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.BASIC;
import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.ROYAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Sql(scripts = "/user-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGradeRepository userGradeRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private final String userId = "test";

    @BeforeEach
    void setup() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    @DisplayName("사용자 저장 성공")
    @Transactional
    void saveUser_success() {
        User user = User.builder()
                .userId("newUser")
                .userPassword("plainPassword")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .userGrade(userGradeRepository.findByGradeName(BASIC))
                .build();

        userService.saveUser(user);

        Optional<User> savedUser = userRepository.findById("newUser");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getUserPassword()).isEqualTo("encodedPassword");
    }

    @Test
    @DisplayName("중복 사용자 저장 실패")
    @Transactional
    void saveUser_alreadyExists() {
        User user = User.builder()
                .userId(userId)
                .userPassword("plainPassword")
                .userName("김철수")
                .userPhoneNumber("01098765432")
                .userEmail("kim@test.com")
                .userBirth(LocalDate.of(1995, 6, 15))
                .userPoint(0)
                .isAuth(false)
                .userStatus(User.Status.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .userGrade(userGradeRepository.findByGradeName(BASIC))
                .build();

        assertThatThrownBy(() -> userService.saveUser(user))
                .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    @DisplayName("사용자 조회 성공")
    void findById_success() {
        Optional<User> user = userService.findById(userId);
        assertThat(user).isPresent();
        assertThat(user.get().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회 실패")
    void findById_notFound() {
        assertThatThrownBy(() -> userService.findById("notExists"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("개인정보 수정")
    @Transactional
    void updatePersonalInformation() {
        User user = userRepository.findById(userId).orElseThrow();
        user.setUserName("수정된이름");

        userService.updatePersonalInformation(user);

        User updated = userRepository.findById(userId).orElseThrow();
        assertThat(updated.getUserName()).isEqualTo("수정된이름");
    }

    @Test
    @DisplayName("개인정보 수정 실패 - 존재하지 않는 사용자")
    void updatePersonalInformation_notFound() {
        User user = new User();
        user.setUserId("notExists");
        user.setUserName("수정된이름");

        assertThatThrownBy(() -> userService.updatePersonalInformation(user))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("로그인 시간 업데이트")
    @Transactional
    void updateLastLoginAt() {
        userService.updateLastLoginAt(userId);

        User updated = userRepository.findById(userId).orElseThrow();
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
    @Transactional
    void updatePoint() {
        userService.updatePoint(userId, 1000);

        User updated = userRepository.findById(userId).orElseThrow();
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
    @Transactional
    void updateGrade() {
        userService.updateUserGradeName(userId, "ROYAL");
        User updated = userRepository.findById(userId).orElseThrow();
        assertThat(updated.getUserGrade().getGradeName()).isEqualTo(ROYAL);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 등급 업데이트 실패")
    void updateGrade_notFound() {
        assertThatThrownBy(() -> userService.updateUserGradeName("unknown", "ROYAL"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 등급 업데이트 실패 - 잘못된 등급")
    void updateGrade_invalidGrade() {
        assertThatThrownBy(() -> userService.updateUserGradeName(userId, "INVALID"))
                .isInstanceOf(UserGradeNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 등급 업데이트 실패 - 저장되지 않은 등급")
    void updateGrade_notSavedGrade() {
        assertThatThrownBy(() -> userService.updateUserGradeName(userId, "PLATINUM"))
                .isInstanceOf(UserGradeNotFoundException.class);
    }

    @Test
    @DisplayName("상태 업데이트")
    @Transactional
    void updateUserStatus() {
        userService.updateUserStatus(userId, User.Status.WITHDRAWN);

        User updated = userRepository.findById(userId).orElseThrow();
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
    @Transactional
    void deleteUser() {
        userService.deleteUser(userId);
        assertThat(userRepository.findById(userId)).isNotPresent();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 삭제 실패")
    void deleteUser_notFound() {
        assertThatThrownBy(() -> userService.deleteUser("unknown"))
                .isInstanceOf(UserNotFoundException.class);
    }
}
