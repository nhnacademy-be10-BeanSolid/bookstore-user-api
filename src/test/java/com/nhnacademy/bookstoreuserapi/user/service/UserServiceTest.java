package com.nhnacademy.bookstoreuserapi.user.service;

import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.service.PointService;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import com.nhnacademy.bookstoreuserapi.user.domain.*;
import com.nhnacademy.bookstoreuserapi.user.exception.PointNotEnoughException;
import com.nhnacademy.bookstoreuserapi.user.exception.UserAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.user.service.impl.UserServiceImpl;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.BASIC;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @MockBean
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGradeRepository userGradeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PointService pointService;

    @Mock
    private PointTypeService pointTypeService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private final String userId = "test";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    private UserGrade createUserGrade(UserGrade.Grade grade) {
        UserGrade userGrade = new UserGrade();
        userGrade.setGradeName(grade);
        userGrade.setRequiredMoney(0L);
        return userGrade;
    }

    private User createUser(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUserPassword("encodedPassword");
        user.setUserName("test");
        user.setUserEmail("test@test.com");
        user.setUserPoint(1000);
        user.setUserStatus(User.Status.ACTIVE);
        user.setUserGrade(createUserGrade(BASIC));
        user.setLastLoginAt(LocalDateTime.now());
        return user;
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
    @DisplayName("사용자 번호로 조회 성공")
    void getUserByUserNo_success() {
        User user = createUser(userId);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseUser response = userService.getUserByUserNo(1L);

        assertThat(response.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("사용자 번호로 조회 실패")
    void getUserByUserNo_notFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByUserNo(2L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("이름+이메일로 아이디 조회 성공")
    void getUserIdByUserNameAndUserEmail_success() {
        User user = createUser(userId);
        when(userRepository.findByUserNameAndUserEmail("홍길동", "hong@test.com")).thenReturn(user);

        ResponseUserId response = userService.getUserIdByUserNameAndUserEmail("홍길동", "hong@test.com");

        assertThat(response.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("이름+이메일로 아이디 조회 실패")
    void getUserIdByUserNameAndUserEmail_notFound() {
        when(userRepository.findByUserNameAndUserEmail("홍길동", "hong@test.com")).thenReturn(null);

        assertThatThrownBy(() -> userService.getUserIdByUserNameAndUserEmail("홍길동", "hong@test.com"))
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
    @DisplayName("개인정보 수정 실패 - 없는 사용자")
    void updatePersonalInformation_notFound() {
        when(userRepository.existsByUserId(userId)).thenReturn(false);

        UserUpdateRequest request = new UserUpdateRequest("pw", "이름", "010", "email", LocalDate.now());

        assertThatThrownBy(() -> userService.updatePersonalInformation(userId, request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신 성공")
    void updateLastLoginAt_success() {
        User user = createUser(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        ResponseUser response = userService.updateLastLoginAt(userId);

        verify(userRepository).updateLastLoginByUserId(eq(userId), any(LocalDateTime.class));
        assertThat(response.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신 실패")
    void updateLastLoginAt_notFound() {
        when(userRepository.existsByUserId(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.updateLastLoginAt(userId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("포인트 적립 성공")
    void plusPoint_success() {
        User user = createUser(userId);

        when(userRepository.findUserIdByUserNo(1L)).thenReturn(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        ResponseUser response = userService.plusPoint(1L, 1000);

        verify(userRepository).updatePointByUserId(userId, 1000);
        assertThat(response.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("포인트 적립 실패 - 사용자 없음")
    void plusPoint_notFound() {
        when(userRepository.findUserIdByUserNo(1L)).thenReturn(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.plusPoint(1L, 1000))
                .isInstanceOf(UserNotFoundException.class);
    }


    @Test
    @DisplayName("포인트 차감 성공")
    void minusPoint_success() {
        User user = createUser(userId);

        when(userRepository.findUserIdByUserNo(1L)).thenReturn(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findUserPointByUserId(userId)).thenReturn(1000);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        ResponseUser response = userService.minusPoint(1L, 500);

        verify(userRepository).updatePointByUserId(userId, -500);
        assertThat(response.getUserId()).isEqualTo(userId);
    }


    @Test
    @DisplayName("포인트 차감 실패 - 사용자 없음")
    void minusPoint_notFound() {
        when(userRepository.findUserIdByUserNo(1L)).thenReturn(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.minusPoint(1L, 1000))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("포인트 차감 실패 - 포인트 부족")
    void minusPoint_notEnough() {
        when(userRepository.findUserIdByUserNo(1L)).thenReturn(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findUserPointByUserId(userId)).thenReturn(100);

        assertThatThrownBy(() -> userService.minusPoint(1L, 200))
                .isInstanceOf(PointNotEnoughException.class);
    }

    @Test
    @DisplayName("상태 변경 성공")
    void updateUserStatus_success() {
        User user = createUser(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        ResponseUser response = userService.updateUserStatus(userId, "WITHDRAWN");

        verify(userRepository).updateStatusByUserId(userId, User.Status.WITHDRAWN);
        assertThat(response.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("상태 변경 실패 - 사용자 없음")
    void updateUserStatus_notFound() {
        when(userRepository.existsByUserId(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.updateUserStatus(userId, "WITHDRAWN"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("회원 탈퇴(deleteUser) 성공")
    void deleteUser_success() {
        User user = createUser(userId);
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        ResponseUser response = userService.deleteUser(userId);

        verify(userRepository).updateStatusByUserId(userId, User.Status.WITHDRAWN);
        assertThat(response.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("회원 존재 여부 확인")
    void isUserExist() {
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        assertThat(userService.isUserExist(userId)).isTrue();
    }

    @Test
    @DisplayName("포인트 조회 성공")
    void getUserPoint_success() {
        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(userRepository.findUserPointByUserId(userId)).thenReturn(1234);

        int point = userService.getUserPoint(userId);

        assertThat(point).isEqualTo(1234);
    }

    @Test
    @DisplayName("포인트 조회 실패 - 사용자 없음")
    void getUserPoint_notFound() {
        when(userRepository.existsByUserId(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.getUserPoint(userId))
                .isInstanceOf(UserNotFoundException.class);
    }
}
