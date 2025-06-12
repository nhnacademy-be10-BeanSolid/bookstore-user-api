package com.nhnacademy.bookstoreuserapi.service;

import com.nhnacademy.bookstoreuserapi.domain.request.PointTypeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.exception.PointTypeNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/point-type-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PointTypeServiceTest {

    @Autowired
    private PointTypeService pointTypeService;

    @Test
    @DisplayName("포인트 타입 전체 조회 성공")
    void testGetAllPointTypes() {
        List<ResponsePointType> list = pointTypeService.getAllPointTypes();

        assertThat(list).isNotEmpty();
        assertThat(list)
                .extracting(ResponsePointType::getTypeName)
                .contains("신규가입", "리뷰작성");
    }

    @Test
    @DisplayName("등급명으로 포인트 타입 조회 성공")
    void testGetPointTypeByGradeName() {
        List<ResponsePointType> list = pointTypeService.getPointTypeByGradeName("GOLD");

        assertThat(list).isNotEmpty();
        assertThat(list).allMatch(pt -> pt.getGradeName().equals("GOLD"));
    }

    @Test
    @DisplayName("포인트 타입 저장 성공")
    void testSavePointType() {
        PointTypeCreateRequest request = new PointTypeCreateRequest(
                "순수 주문금액",
                200L,
                20,
                "BRONZE"
        );

        pointTypeService.savePointType(request);

        List<ResponsePointType> list = pointTypeService.getAllPointTypes();

        assertThat(list)
                .extracting(ResponsePointType::getTypeName)
                .contains("순수 주문금액");
    }

    @Test
    @DisplayName("earningPoint 업데이트 성공")
    void testUpdateEarningPoint() {
        ResponsePointType updated = pointTypeService.updateEarningPoint(999L, 1L);

        assertThat(updated.getEarningPoint()).isEqualTo(999L);
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

        List<ResponsePointType> list = pointTypeService.getAllPointTypes();
        assertThat(list).noneMatch(pt -> pt.getTypeId().equals(1L));
    }

    @Test
    @DisplayName("포인트 타입 삭제 실패 - 존재하지 않는 ID")
    void testDeletePointTypeNotFound() {
        assertThatThrownBy(() -> pointTypeService.deletePointType(999L))
                .isInstanceOf(PointTypeNotFoundException.class);
    }
}
