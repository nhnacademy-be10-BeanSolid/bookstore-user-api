package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"/user-test.sql", "/point-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
class PointServiceTest {

    @Autowired
    private PointService pointService;

    private final String userId = "test";

//    @Test
    @DisplayName("포인트 전체 조회 성공")
    void testFindAllPointsByUserId() {
        Pageable pageable = PageRequest.of(0, 10);

        List<ResponsePoint> points = pointService.findAll(userId, pageable).getContent();

        assertThat(points).hasSize(2);
        assertThat(points)
                .extracting(ResponsePoint::getEarnedAndUsedPoint)
                .containsExactlyInAnyOrder(500, -100);
    }

//    @Test
    @DisplayName("존재하지 않는 사용자 포인트 조회 실패")
    void testFindAllPointsByInvalidUserId() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThatThrownBy(() -> pointService.findAll("unknown", pageable))
                .isInstanceOf(UserNotFoundException.class);
    }

//    @Test
    @DisplayName("포인트 저장 성공")
    void testSavePoint_success() {
        PointCreateRequest request = new PointCreateRequest(
                userId,
                2L,
                1L,
                LocalDateTime.of(2025, 1, 1, 10, 0, 15),
                1000
        );

        ResponsePoint response = pointService.savePoint(userId, request);

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getPaymentId()).isEqualTo(2L);
        assertThat(response.getEarnedAndUsedPoint()).isEqualTo(1000L);
        Pageable pageable = PageRequest.of(0, 10);


        // 저장 후 조회 시 개수가 3개인지 확인
        List<ResponsePoint> points = pointService.findAll(userId, pageable).getContent();
        assertThat(points).hasSize(3);
    }

//    @Test
    @DisplayName("존재하지 않는 사용자 포인트 저장 실패")
    void testSavePoint_userNotFound() {
        PointCreateRequest request = new PointCreateRequest(
                "unknownUser",
                1L,
                1L,
                LocalDateTime.now(),
                200
        );

        assertThatThrownBy(() -> pointService.savePoint("unknownUser", request))
                .isInstanceOf(UserNotFoundException.class);
    }
}
