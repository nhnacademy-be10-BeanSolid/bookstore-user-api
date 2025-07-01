package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.config.QuerydslConfig;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.domain.User.Status;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
        user.setLastLoginAt(LocalDateTime.now().minusDays(1));
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
}
