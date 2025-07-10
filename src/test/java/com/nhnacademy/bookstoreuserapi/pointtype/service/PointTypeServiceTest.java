package com.nhnacademy.bookstoreuserapi.pointtype.service;

import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointTypeUpdateRequest;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointType;
import com.nhnacademy.bookstoreuserapi.pointtype.exception.PointTypeNotFoundException;
import com.nhnacademy.bookstoreuserapi.pointtype.repository.PointTypeRepository;
import com.nhnacademy.bookstoreuserapi.pointtype.service.impl.PointTypeServiceImpl;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.GOLD;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointTypeServiceTest {

    @InjectMocks
    private PointTypeServiceImpl pointTypeService;

    @Mock
    private PointTypeRepository pointTypeRepository;

    @Mock
    private UserGradeRepository userGradeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("포인트 타입 전체 조회 성공")
    void testGetAllPointTypes() {
        List<PointType> pointTypes = List.of(
                createPointType(1L, "회원가입", 100, 5, GOLD, true),
                createPointType(2L, "리뷰작성", 200, 10, GOLD, true)
        );
        Page<PointType> page = new PageImpl<>(pointTypes);
        when(pointTypeRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ResponsePointType> result = pointTypeService.getAllPointTypes(PageRequest.of(0, 10));

        assertThat(result).isNotEmpty();
        assertThat(result).extracting(ResponsePointType::getTypeName)
                .contains("회원가입", "리뷰작성");
        verify(pointTypeRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("등급명으로 포인트 타입 조회 성공")
    void testGetPointTypeByGradeName() {
        Page<ResponsePointType> mockPage = new PageImpl<>(List.of(
                new ResponsePointType(1L, "가입", 100, 5, GOLD.name(), true)
        ));
        when(pointTypeRepository.findPointTypeByGradeName(eq(GOLD), any(Pageable.class)))
                .thenReturn(mockPage);

        Page<ResponsePointType> result = pointTypeService.getPointTypeByGradeName(GOLD, PageRequest.of(0, 10));

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(pt -> pt.getGradeName().equals(GOLD.name()));
        verify(pointTypeRepository).findPointTypeByGradeName(eq(GOLD), any(Pageable.class));
    }

    @Test
    @DisplayName("포인트 타입 저장 성공")
    void testSavePointType() {
        PointTypeCreateRequest req = new PointTypeCreateRequest("순수 주문금액", 2000, 20, "ROYAL", true);
        UserGrade userGrade = new UserGrade();
        userGrade.setGradeName(UserGrade.Grade.ROYAL);

        when(userGradeRepository.findByGradeName(UserGrade.Grade.ROYAL)).thenReturn(userGrade);

        pointTypeService.savePointType(req);

        verify(pointTypeRepository).save(any(PointType.class));
    }

    @Test
    @DisplayName("포인트 타입 삭제 성공")
    void testDeletePointType() {
        when(pointTypeRepository.existsById(1L)).thenReturn(true);

        pointTypeService.deletePointType(1L);

        verify(pointTypeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("포인트 타입 삭제 실패 - 존재하지 않는 ID")
    void testDeletePointTypeNotFound() {
        when(pointTypeRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> pointTypeService.deletePointType(999L))
                .isInstanceOf(PointTypeNotFoundException.class);
    }

    @Test
    @DisplayName("포인트 타입 isActive 상태 조회 성공")
    void testGetPointTypeIsActive() {
        when(pointTypeRepository.existsById(1L)).thenReturn(true);
        when(pointTypeRepository.findIsActiveByTypeId(1L)).thenReturn(true);

        Boolean isActive = pointTypeService.getPointTypeIsActive(1L);

        assertThat(isActive).isTrue();
    }

    @Test
    @DisplayName("포인트 타입 earningPoint 조회 성공")
    void testGetEarningPointByTypeName() {
        when(pointTypeRepository.existsByTypeName("회원가입")).thenReturn(true);
        when(pointTypeRepository.findEarningPointByTypeName("회원가입")).thenReturn(100);

        int earningPoint = pointTypeService.getEarningPointByTypeName("회원가입");

        assertThat(earningPoint).isEqualTo(100);
    }

    @Test
    @DisplayName("포인트 타입 isActive 상태 변경 성공")
    void testUpdatePointTypeIsActive() {
        PointType pt = createPointType(1L, "회원가입", 100, 5, GOLD, true);
        when(pointTypeRepository.findById(1L)).thenReturn(Optional.of(pt));
        when(pointTypeRepository.existsById(1L)).thenReturn(true);
        when(pointTypeRepository.findIsActiveByTypeId(1L)).thenReturn(true);

        pointTypeService.updatePointTypeisActive(1L);

        assertThat(pt.getIsActive()).isFalse();
    }

    @Test
    @DisplayName("포인트 타입 정보 수정 성공")
    void testUpdatePointTypeInfo() {
        PointType pt = createPointType(1L, "회원가입", 100, 5, GOLD, true);
        when(pointTypeRepository.findById(1L)).thenReturn(Optional.of(pt));
        UserGrade userGrade = new UserGrade();
        userGrade.setGradeName(GOLD);
        when(userGradeRepository.findByGradeName(GOLD)).thenReturn(userGrade);

        PointTypeUpdateRequest req = new PointTypeUpdateRequest("수정타입", 500, 15, "GOLD");
        pointTypeService.updatePointTypeInfo(req, 1L);

        assertThat(pt.getTypeName()).isEqualTo("수정타입");
        assertThat(pt.getEarningPoint()).isEqualTo(500);
        assertThat(pt.getEarningRate()).isEqualTo(15);
        assertThat(pt.getUserGrade().getGradeName()).isEqualTo(GOLD);
    }

    // 헬퍼 메서드
    private PointType createPointType(Long id, String name, int point, int rate, UserGrade.Grade grade, boolean isActive) {
        PointType pt = new PointType();
        pt.setTypeId(id);
        pt.setTypeName(name);
        pt.setEarningPoint(point);
        pt.setEarningRate(rate);
        pt.setIsActive(isActive);
        UserGrade userGrade = new UserGrade();
        userGrade.setGradeName(grade);
        pt.setUserGrade(userGrade);
        return pt;
    }
}
