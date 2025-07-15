package com.nhnacademy.bookstoreuserapi.point.service;

import com.nhnacademy.bookstoreuserapi.point.domain.Point;
import com.nhnacademy.bookstoreuserapi.point.domain.PointCreateRequest;
import com.nhnacademy.bookstoreuserapi.point.domain.ResponsePoint;
import com.nhnacademy.bookstoreuserapi.point.repository.PointRepository;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointType;
import com.nhnacademy.bookstoreuserapi.pointtype.exception.PointTypeNotFoundException;
import com.nhnacademy.bookstoreuserapi.pointtype.repository.PointTypeRepository;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.point.service.impl.PointServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PointServiceTest {

    @InjectMocks
    private PointServiceImpl pointService;

    @Mock private PointRepository pointRepository;
    @Mock private UserRepository userRepository;
    @Mock private PointTypeRepository pointTypeRepository;

    private final String userId = "test";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUserNo(1L);
        return user;
    }

    private PointType createPointType(Long typeId) {
        PointType pt = new PointType();
        pt.setTypeId(typeId);
        pt.setTypeName("적립타입");
        return pt;
    }

    private Point createPoint(Long pointId, User user, PointType pointType, Long orderNo, LocalDateTime at, String value) {
        Point point = new Point();
        point.setPointId(pointId);
        point.setUser(user);
        point.setPointType(pointType);
        point.setOrderId(orderNo);
        point.setEarnedAndUsedAt(at);
        point.setEarnedAndUsedPoint(value);
        return point;
    }

    @Test
    @DisplayName("포인트 전체 조회 성공")
    void testFindAllPointsByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = createUser(userId);

        PointType ptType = createPointType(1L);
        Point p1 = createPoint(1L, user, ptType, 1L, LocalDateTime.now(), "500");
        Point p2 = createPoint(2L, user, ptType, 2L, LocalDateTime.now(), "-100");

        List<ResponsePoint> responsePoints = List.of(
                new ResponsePoint(1L, userId, 1L, 1L, p1.getEarnedAndUsedAt(), "500"),
                new ResponsePoint(2L, userId, 1L, 2L, p2.getEarnedAndUsedAt(), "-100")
        );
        Page<ResponsePoint> mockPage = new PageImpl<>(responsePoints, pageable, 2);

        when(userRepository.existsByUserId(userId)).thenReturn(true);
        when(pointRepository.findPointByUserId(eq(userId), any(Pageable.class))).thenReturn(mockPage);

        List<ResponsePoint> points = pointService.findAll(userId, pageable).getContent();

        assertThat(points).hasSize(2);
        assertThat(points)
                .extracting(ResponsePoint::getEarnedAndUsedPoint)
                .containsExactlyInAnyOrder("500", "-100");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 포인트 조회 실패")
    void testFindAllPointsByInvalidUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.existsByUserId("unknown")).thenReturn(false);

        assertThatThrownBy(() -> pointService.findAll("unknown", pageable))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("포인트 저장 성공")
    void testSavePoint_success() {
        User user = createUser(userId);
        PointType pointType = createPointType(2L);
        PointCreateRequest request = new PointCreateRequest(
                userId, 2L, 1L, LocalDateTime.of(2025, 1, 1, 10, 0, 15), "1000"
        );
        Point savedPoint = createPoint(3L, user, pointType, 2L, request.earnedAndUsedAt(), "1000");

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(pointTypeRepository.findById(2L)).thenReturn(Optional.of(pointType));
        when(pointRepository.save(any(Point.class))).thenReturn(savedPoint);

        ResponsePoint response = pointService.savePoint(userId, request);

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getOrderId()).isEqualTo(2L);
        assertThat(response.getEarnedAndUsedPoint()).isEqualTo("1000");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 포인트 저장 실패")
    void testSavePoint_userNotFound() {
        PointCreateRequest request = new PointCreateRequest(
                "unknownUser", 1L, 1L, LocalDateTime.now(), "200"
        );
        when(userRepository.findByUserId("unknownUser")).thenReturn(null);

        assertThatThrownBy(() -> pointService.savePoint("unknownUser", request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 포인트타입 저장 실패")
    void testSavePoint_pointTypeNotFound() {
        User user = createUser(userId);
        PointCreateRequest request = new PointCreateRequest(
                userId, 999L, 1L, LocalDateTime.now(), "200"
        );
        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(pointTypeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pointService.savePoint(userId, request))
                .isInstanceOf(PointTypeNotFoundException.class);
    }
}
