package com.nhnacademy.bookstoreuserapi.user.repository;

import com.nhnacademy.bookstoreuserapi.common.config.QuerydslConfig;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.domain.User.Status;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.BASIC;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGradeRepository userGradeRepository;

    private final String userId = "testUser";

    @BeforeEach
    void setUp() {
        User user = new User(
                userId,
                "password",
                "홍길동",
                "01012345678",
                "hong@test.com",
                LocalDate.of(1995, 5, 1)
        );
        user.setUserStatus(Status.ACTIVE);
        user.setUserPoint(100);
        user.setCreatedAt(LocalDateTime.now().minusDays(1));
        UserGrade userGrade = new UserGrade(BASIC, 0L);

        userGradeRepository.save(userGrade);

        user.setUserGrade(userGrade);

        userRepository.save(user);
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신")
    void testUpdateLastLoginByUserId() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        userRepository.updateLastLoginByUserId(userId, now);

        User updated = userRepository.findByUserId(userId);
        assertThat(updated).isNotNull();
        assertThat(updated.getLastLoginAt().truncatedTo(ChronoUnit.SECONDS)).isEqualTo(now);
    }

    @Test
    @DisplayName("포인트 추가")
    void testUpdatePointByUserId() {
        userRepository.updatePointByUserId(userId, 50);

        User updated = userRepository.findByUserId(userId);
        assertThat(updated).isNotNull();
        assertThat(updated.getUserPoint()).isEqualTo(150);
    }

    @Test
    @DisplayName("상태 변경")
    void testUpdateStatusByUserId() {
        userRepository.updateStatusByUserId(userId, Status.WITHDRAWN);

        User updated = userRepository.findByUserId(userId);
        assertThat(updated).isNotNull();
        assertThat(updated.getUserStatus()).isEqualTo(Status.WITHDRAWN);
    }

    @Test
    @DisplayName("유저 아이디 존재 여부 확인")
    void testExistsByUserId() {
        boolean exists = userRepository.existsByUserId(userId);
        assertThat(exists).isTrue();

        boolean notExists = userRepository.existsByUserId("nonexistentuser");
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("유저 이름과 이메일로 유저 찾기")
    void testFindByUserNameAndUserEmail() {
        User foundUser = userRepository.findByUserNameAndUserEmail("홍길동", "hong@test.com");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("유저 아이디로 포인트 조회")
    void testFindUserPointByUserId() {
        int userPoint = userRepository.findUserPointByUserId(userId);
        assertThat(userPoint).isEqualTo(100);
    }

    @Test
    @DisplayName("유저 번호로 아이디 조회")
    void testFindUserIdByUserNo() {
        User user = userRepository.findByUserId(userId);
        String foundUserId = userRepository.findUserIdByUserNo(user.getUserNo());
        assertThat(foundUserId).isEqualTo(userId);
    }

    @Test
    @DisplayName("상태와 마지막 로그인 시간으로 유저 찾기")
    void testFindByUserStatusAndLastLoginAtBefore() {
        User user = userRepository.findByUserId(userId);
        user.setLastLoginAt(LocalDateTime.now().minusMonths(4));
        userRepository.save(user);

        Page<User> users = userRepository.findByUserStatusAndLastLoginAtBefore(Status.ACTIVE, LocalDateTime.now().minusMonths(3), PageRequest.of(0, 10));
        assertThat(users.getContent()).hasSize(1);
        assertThat(users.getContent().get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("유저 상태 업데이트")
    void testUpdateStatusForUsers() {
        User user1 = new User("user1", "pw", "User One", "01011111111", "user1@test.com", LocalDate.now());
        user1.setCreatedAt(LocalDateTime.now().minusDays(1));
        user1.setUserGrade(new UserGrade(BASIC, 0L));
        user1.setUserStatus(Status.ACTIVE);
        userRepository.save(user1);

        User user2 = new User("user2", "pw", "User Two", "01022222222", "user2@test.com", LocalDate.now());
        user2.setCreatedAt(LocalDateTime.now().minusDays(1));
        user2.setUserGrade(new UserGrade(BASIC, 0L));
        user2.setUserStatus(Status.ACTIVE);
        userRepository.save(user2);

        List<Long> userIds = Arrays.asList(user1.getUserNo(), user2.getUserNo());
        userRepository.updateStatusForUsers(userIds);

        User updatedUser1 = userRepository.findByUserId("user1");
        User updatedUser2 = userRepository.findByUserId("user2");

        assertThat(updatedUser1.getUserStatus()).isEqualTo(Status.DORMANT);
        assertThat(updatedUser2.getUserStatus()).isEqualTo(Status.DORMANT);
    }

    @Test
    @DisplayName("생일인 유저 찾기")
    void testFindByBirthMonthAndBirthDay() {
        LocalDate today = LocalDate.now();
        User birthdayUser = new User("birthdayUser", "pw", "Birthday User", "01033333333", "birthday@test.com", today);
        birthdayUser.setCreatedAt(LocalDateTime.now().minusDays(1));
        birthdayUser.setUserGrade(new UserGrade(BASIC, 0L));
        birthdayUser.setUserStatus(Status.ACTIVE);
        userRepository.save(birthdayUser);

        List<User> users = userRepository.findByBirthMonthAndBirthDay(today.getMonthValue(), today.getDayOfMonth());
        assertThat(users).hasSize(1);
        assertThat(users.getFirst().getUserId()).isEqualTo("birthdayUser");
    }
}
