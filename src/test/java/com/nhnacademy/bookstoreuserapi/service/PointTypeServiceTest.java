package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.pointtype.exception.PointTypeNotFoundException;
import com.nhnacademy.bookstoreuserapi.pointtype.service.PointTypeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.GOLD;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Sql(scripts = "/user-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
class PointTypeServiceTest {

    @Autowired
    private PointTypeService pointTypeService;

    @Test
    @DisplayName("포인트 타입 전체 조회 성공")
    void testGetAllPointTypes() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ResponsePointType> list = pointTypeService.getAllPointTypes(pageable);

        assertThat(list).isNotEmpty();
        assertThat(list)
                .extracting(ResponsePointType::getTypeName)
                .contains("회원가입", "리뷰작성");
    }

    @Test
    @DisplayName("등급명으로 포인트 타입 조회 성공")
    void testGetPointTypeByGradeName() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResponsePointType> list = pointTypeService.getPointTypeByGradeName(GOLD, pageable);

        assertThat(list)
                .isNotEmpty()
                .allMatch(pt -> pt.getGradeName().equals(GOLD.toString()));
    }

    @Test
    @DisplayName("포인트 타입 저장 성공")
    void testSavePointType() {
        PointTypeCreateRequest request = new PointTypeCreateRequest(
                "순수 주문금액",
                2000,
                20,
                "ROYAL"
        );

        pointTypeService.savePointType(request);
        Pageable pageable = PageRequest.of(0, 10);

        Page<ResponsePointType> list = pointTypeService.getAllPointTypes(pageable);

        assertThat(list)
                .extracting(ResponsePointType::getTypeName)
                .contains("순수 주문금액");
    }

    @Test
    @DisplayName("earningPoint 업데이트 성공")
    void testUpdateEarningPoint() {
        ResponsePointType updated = pointTypeService.updateEarningPoint(999, 1L);

        assertThat(updated.getEarningPoint()).isEqualTo(999);
    }

    @Test
    @DisplayName("earningRate 업데이트 성공")
    void testUpdateEarningRate() {
        ResponsePointType updated = pointTypeService.updateEarningRate(15, 2L);

        assertThat(updated.getEarningRate()).isEqualTo(15);
    }

    @Test
    @DisplayName("earningRate 업데이트 실패 - 존재하지 않는 ID")
    void testUpdateEarningRateNotFound() {
        assertThatThrownBy(() -> pointTypeService.updateEarningRate(20, 999L))
                .isInstanceOf(PointTypeNotFoundException.class);
    }

    @Test
    @DisplayName("포인트 타입 삭제 성공")
    void testDeletePointType() {
        // typeId = 1이 존재한다고 가정
        pointTypeService.deletePointType(1L);
        Pageable pageable = PageRequest.of(0, 10);


        Page<ResponsePointType> list = pointTypeService.getAllPointTypes(pageable);
        assertThat(list).noneMatch(pt -> pt.getTypeId().equals(1L));
    }

    @Test
    @DisplayName("포인트 타입 삭제 실패 - 존재하지 않는 ID")
    void testDeletePointTypeNotFound() {
        assertThatThrownBy(() -> pointTypeService.deletePointType(999L))
                .isInstanceOf(PointTypeNotFoundException.class);
    }
}
