package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.config.QuerydslConfig;
import com.nhnacademy.bookstoreuserapi.domain.entity.Point;
import com.nhnacademy.bookstoreuserapi.domain.entity.PointType;
import com.nhnacademy.bookstoreuserapi.domain.entity.User;
import com.nhnacademy.bookstoreuserapi.domain.entity.User.Status;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.BASIC;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class PointRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private UserGradeRepository userGradeRepository;

    @Autowired
    private PointTypeRepository pointTypeRepository;

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
        UserGrade userGrade = new UserGrade(BASIC,0L);

        userGradeRepository.save(userGrade);

        user.setUserGrade(userGrade);

        userRepository.save(user);

        PointType pointType1 = new PointType(null, "회원가입", 5000L, 1, userGrade);
        PointType pointType2 = new PointType(null,"리뷰작성", 500L, 1, userGrade);

        pointTypeRepository.save(pointType1);
        pointTypeRepository.save(pointType2);

        Point point1 = new Point(null, user, pointType1, 1L, LocalDateTime.now(), 50L);
        Point point2 = new Point(null, user, pointType2, 2L, LocalDateTime.now(), -100L);

        pointRepository.save(point1);
        pointRepository.save(point2);
    }

    @Test
    @DisplayName("사용자 ID로 포인트 조회")
    void testFindPointByUserId() {
        Pageable pageable = PageRequest.of(0, 10);

        List<ResponsePoint> points = pointRepository.findPointByUserId(userId, pageable).getContent();

        assertThat(points).hasSize(2);
        assertThat(points)
                .extracting("earnedAndUsedPoint")
                .containsExactlyInAnyOrder(50L, -100L);
    }
}
