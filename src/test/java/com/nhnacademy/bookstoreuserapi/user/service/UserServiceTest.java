package com.nhnacademy.bookstoreuserapi.user.service;

import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.user.domain.*;
import com.nhnacademy.bookstoreuserapi.user.exception.UserAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.user.service.impl.UserServiceImpl;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.usergrade.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.BASIC;
import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.ROYAL;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock private UserRepository userRepository;
    @Mock private UserGradeRepository userGradeRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private PointService pointService;
    @Mock private PointTypeService pointTypeService;

    private final String userId = "test";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    private UserGrade createUserGrade(UserGrade.Grade grade) {
        UserGrade userGrade = new UserGrade();
        userGrade.setGradeName(grade);
        userGrade.setRequiredMoney(0L); // 필요하다면 추가 세팅
        return userGrade;
    }

    @Test
    @DisplayName("사용자 저장 성공 (회원가입 포인트 적립)")
    void saveUser_success_withWelcomePoint() {
        UserCreateRequest request = new UserCreateRequest("newUser", "plainPassword", "김철수", "01098765432", "kim@test.com", LocalDate.of(1995, 6, 15));
        UserGrade basicGrade = createUserGrade(BASIC);
        when(userRepository.existsByUserId("newUser")).thenReturn(false);
        when(userGradeRepository.findByGradeName(BASIC)).thenReturn(basicGrade);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(pointTypeService.isActivePointType("회원가입")).thenReturn(true);
        when(pointTypeService.getEarningPointByTypeName("회원가입")).thenReturn(100);
        when(pointTypeService.getTypeIdByName("회원가입")).thenReturn(1L);

        userService.saveUser(request);

        verify(userRepository).save(any(User.class));
        verify(pointService).savePoint(eq("newUser"), any(PointCreateRequest.class));
    }

    @Test
    @DisplayName("중복 사용자 저장 실패")
    void saveUser_alreadyExists() {
        UserCreateRequest request = new UserCreateRequest(userId, "testPassword", "test", "01098765432", "test@test.com", LocalDate.of(1995, 6, 15));
        when(userRepository.existsByUserId(userId)).thenReturn(true);

        assertThatThrownBy(() -> userService.saveUser(request))
                .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    @DisplayName("Oauth2 사용자 저장 성공 (포인트 적립)")
    void saveOauth2User_success_withWelcomePoint() {
        Oauth2UserCreateRequest request = new Oauth2UserCreateRequest("PAYCO", "pid", "홍길동", "01011112222", "hong@test.com", LocalDate.of(1990, 1, 1));
        UserGrade basicGrade = createUserGrade(BASIC);
        String oauthId = "PAYCOpid";
        when(userRepository.existsByUserId(oauthId)).thenReturn(false);
        when(userGradeRepository.findByGradeName(BASIC)).thenReturn(basicGrade);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(pointTypeService.isActivePointType("회원가입")).thenReturn(true);
        when(pointTypeService.getEarningPointByTypeName("회원가입")).thenReturn(100);
        when(pointTypeService.getTypeIdByName("회원가입")).thenReturn(1L);

        userService.saveOauth2User(request);

        verify(userRepository).save(any(User.class));
        verify(pointService).savePoint(eq(oauthId), any(PointCreateRequest.class));
    }

    @Test
    @DisplayName("사용자 정보 조회 성공")
    void getUser_success() {
        User user = createUser(userId);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        ResponseUser response = userService.getUser(userId);

        assertThat(response.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("사용자 정보 조회 실패")
    void getUser_notFound() {
        when(userRepository.findByUserId("notExists")).thenReturn(null);

        assertThatThrownBy(() -> userService.getUser("notExists"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("개인정보 수정")
    void updatePersonalInformation() {
        User user = createUser(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        UserUpdateRequest request = new UserUpdateRequest("newPassword", "수정이름", "01012345678", "updated@test.com", LocalDate.of(1993, 12, 12));

        ResponseUser updated = userService.updatePersonalInformation(userId, request);

        assertThat(updated.getUserName()).isEqualTo("수정이름");
        assertThat(user.getUserPassword()).isEqualTo("encodedPassword");
    }

    @Test
    @DisplayName("포인트 업데이트")
    void updatePoint() {
        User user = createUser(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        userService.updatePoint(userId, 1000);

        verify(userRepository).updatePointByUserId(userId, 1000);
    }

    @Test
    @DisplayName("회원 등급 업데이트 성공")
    void updateUserGradeName_success() {
        User user = createUser(userId);
        UserGrade grade = createUserGrade(ROYAL);
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userGradeRepository.findByGradeName(ROYAL)).thenReturn(grade);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        userService.updateUserGradeName(userId, "ROYAL");

        verify(userRepository).updateUserGrade_gradeNameByUserId(userId, ROYAL);
    }

    @Test
    @DisplayName("회원 등급 업데이트 실패 - 잘못된 등급명")
    void updateUserGradeName_invalidGrade() {
        when(userRepository.existsByUserId(userId)).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUserGradeName(userId, "INVALID"))
                .isInstanceOf(UserGradeNotFoundException.class);
    }

    @Test
    @DisplayName("회원 등급 업데이트 실패 - 저장되지 않은 등급")
    void updateUserGradeName_notSavedGrade() {
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userGradeRepository.findByGradeName(any())).thenReturn(null);

        assertThatThrownBy(() -> userService.updateUserGradeName(userId, "PLATINUM"))
                .isInstanceOf(UserGradeNotFoundException.class);
    }

    @Test
    @DisplayName("사용자 삭제(상태 변경)")
    void deleteUser() {
        User user = createUser(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        userService.deleteUser(userId);

        verify(userRepository).updateStatusByUserId(userId, User.Status.WITHDRAWN);
    }

    // UserGrade의 gradeName이 반드시 null이 아니게 생성
    private User createUser(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUserPassword("encodedPassword");
        user.setUserName("test");
        user.setUserEmail("test@test.com");
        user.setUserPoint(0);
        user.setUserStatus(User.Status.ACTIVE);
        user.setUserGrade(createUserGrade(BASIC));
        user.setLastLoginAt(LocalDateTime.now());
        return user;
    }
}
