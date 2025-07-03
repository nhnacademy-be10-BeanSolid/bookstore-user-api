package com.nhnacademy.bookstoreuserapi.pointtype.repository;

import com.nhnacademy.bookstoreuserapi.common.config.QuerydslConfig;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.PointType;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.pointtype.domain.ResponsePointType;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.BASIC;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
class PointTypeRepositoryTest {

    @Autowired
    private PointTypeRepository pointTypeRepository;

    @Autowired
    private UserGradeRepository userGradeRepository;

    @BeforeEach
    void setUp() {
        PointType pointType = new PointType();
        pointType.setTypeName("순수금액");
        pointType.setEarningPoint(100);
        pointType.setEarningRate(1);

        UserGrade userGrade = new UserGrade();
        userGrade.setGradeName(BASIC);
        userGrade.setRequiredMoney(100L);

        pointType.setUserGrade(userGrade);

        userGradeRepository.save(userGrade);
        pointTypeRepository.save(pointType);
    }

    @Test
    @DisplayName("등급 이름으로 PointType 조회")
    void testFindByGradeName() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResponsePointType> result = pointTypeRepository.findPointTypeByGradeName(BASIC, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().getFirst().getTypeName()).isEqualTo("순수금액");
    }
}
