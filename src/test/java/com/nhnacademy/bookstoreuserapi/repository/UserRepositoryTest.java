package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.User.Status;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.BASIC;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = "/user-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

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
        user.setOrderMoney(0);
        UserGrade userGrade = new UserGrade(BASIC, 0L);
        user.setUserGrade(userGrade);

        userRepository.save(user);
    }

    @Test
    @DisplayName("마지막 로그인 시간 갱신")
    void testUpdateLastLoginByUserId() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        userRepository.updateLastLoginByUserId(userId, now);

        Optional<User> updated = userRepository.findById(userId);
        assertThat(updated).isPresent();
        assertThat(updated.get().getLastLoginAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("포인트 추가")
    void testUpdatePointByUserId() {
        userRepository.updatePointByUserId(userId, 50);

        Optional<User> updated = userRepository.findById(userId);
        assertThat(updated).isPresent();
        assertThat(updated.get().getUserPoint()).isEqualTo(150);
    }

    @Test
    @DisplayName("상태 변경")
    void testUpdateStatusByUserId() {
        userRepository.updateStatusByUserId(userId, Status.WITHDRAWN);

        Optional<User> updated = userRepository.findById(userId);
        assertThat(updated).isPresent();
        assertThat(updated.get().getUserStatus()).isEqualTo(Status.WITHDRAWN);
    }
}
