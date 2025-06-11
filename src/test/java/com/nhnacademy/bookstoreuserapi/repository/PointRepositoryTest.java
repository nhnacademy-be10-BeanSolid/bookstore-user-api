package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.Point;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.User.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PointRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    private final String userId = "testUser";

    @BeforeEach
    void setUp() {
        User user = new User(
                userId,
                "password",
                "홍길동",
                "01012345678",
                "hong@test.com",
                java.time.LocalDate.of(1995, 5, 1)
        );
        user.setUserStatus(Status.ACTIVE);
        user.setUserPoint(100);
        user.setLastLoginAt(LocalDateTime.now());

        userRepository.save(user);

        Point point1 = new Point(null, user, 1L, 1L, LocalDateTime.now(), 50L);
        Point point2 = new Point(null, user, 2L, 2L, LocalDateTime.now(), -100L);

        pointRepository.save(point1);
        pointRepository.save(point2);
    }

    @Test
    @DisplayName("사용자 ID로 포인트 조회")
    void testFindPointByUserId() {
        List<Point> points = pointRepository.findPointByUserId(userId);

        assertThat(points).hasSize(2);
        assertThat(points)
                .extracting("earnedAndUsedPoint")
                .containsExactlyInAnyOrder(50L, -100L);
    }
}
