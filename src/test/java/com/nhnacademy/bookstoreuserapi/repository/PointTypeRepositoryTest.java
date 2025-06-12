package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.domain.entity.PointType;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.BASIC;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PointTypeRepositoryTest {

    @Autowired
    private PointTypeRepository pointTypeRepository;

    @Autowired
    private UserGradeRepository userGradeRepository;

    private Long savedTypeId;

    @BeforeEach
    void setUp() {
        PointType pointType = new PointType();
        pointType.setTypeName("회원가입 포인트");
        pointType.setEarningPoint(100L);
        pointType.setEarningRate(10);

        UserGrade userGrade = new UserGrade();
        userGrade.setGradeName(BASIC);
        userGrade.setRequiredMoney(100L);

        pointType.setUserGrade(userGrade);

        userGradeRepository.save(userGrade);
        savedTypeId = pointTypeRepository.save(pointType).getTypeId();
    }

    @Test
    @DisplayName("등급 이름으로 PointType 조회")
    void testFindByGradeName() {
        List<PointType> result = pointTypeRepository.findPointTypeByGradeName(BASIC);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTypeName()).isEqualTo("회원가입 포인트");
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("earningPoint 업데이트 테스트")
    void testUpdateEarningPoint() {
        pointTypeRepository.updateEarningPoint(500L, savedTypeId);

        PointType updated = pointTypeRepository.findById(savedTypeId).orElseThrow();

        assertThat(updated.getEarningPoint()).isEqualTo(500L);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("earningRate 업데이트 테스트")
    void testUpdateEarningRate() {
        pointTypeRepository.updateEarningRate(25, savedTypeId);

        PointType updated = pointTypeRepository.findById(savedTypeId).orElseThrow();

        assertThat(updated.getEarningRate()).isEqualTo(25);
    }
}
