package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.config.QuerydslConfig;
import com.nhnacademy.bookstoreuserapi.domain.entity.PointType;
import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponsePointType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade.Grade.BASIC;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
class PointTypeRepositoryTest {

    @Autowired
    private PointTypeRepository pointTypeRepository;

    @Autowired
    private UserGradeRepository userGradeRepository;

    private Long savedTypeId;

    @BeforeEach
    void setUp() {
        PointType pointType = new PointType();
        pointType.setTypeName("순수금액");
        pointType.setEarningPoint(100L);
        pointType.setEarningRate(1);

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
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResponsePointType> result = pointTypeRepository.findPointTypeByGradeName(BASIC, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().getFirst().getTypeName()).isEqualTo("순수금액");
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
