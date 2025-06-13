package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUserGrade;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.UserGradeRepository;
import com.nhnacademy.bookstoreuserapi.service.UserGradeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserGradeServiceImpl implements UserGradeService {
    private final UserGradeRepository userGradeRepository;

    @Override
    public ResponseUserGrade saveUserGrade(UserGradeCreateRequest userGrade) {
        if(userGradeRepository.existsByGradeName(UserGrade.Grade.valueOf(userGrade.gradeName()))){
            throw new UserGradeAlreadyExistException(userGrade.gradeName());
        }
        UserGrade savedUserGrade = userGradeRepository.save(new UserGrade(userGrade));
        return new ResponseUserGrade(
                savedUserGrade.getGradeName().name(),
                savedUserGrade.getRequiredMoney()
        );
    }

    @Override
    public ResponseUserGrade updateUserGrade(String gradeName, UserGradeUpdateRequest userGrade) {
        UserGrade findUserGrade = userGradeRepository.findById(UserGrade.Grade.valueOf(gradeName))
                .orElseThrow(() -> new UserGradeNotFoundException(gradeName));
        if(userGradeRepository.existsByGradeName(UserGrade.Grade.valueOf(userGrade.gradeName())) &&
                !findUserGrade.getGradeName().name().equals(userGrade.gradeName())){
            throw new UserGradeAlreadyExistException(userGrade.gradeName());
        }

        findUserGrade.setGradeName(UserGrade.Grade.valueOf(userGrade.gradeName()));
        findUserGrade.setRequiredMoney(userGrade.requiredMoney());

        log.warn("주의: 등급 {}의 금액이 위계상 비정상일 수 있음. (입력값: {})", userGrade.gradeName(), userGrade.requiredMoney());

        return new ResponseUserGrade(
                findUserGrade.getGradeName().name(),
                findUserGrade.getRequiredMoney()
        );
    }

    @Override
    public ResponseUserGrade getUserGrade(String gradeName) {
        UserGrade findUserGrade = userGradeRepository.findById(UserGrade.Grade.valueOf(gradeName))
                .orElseThrow(() -> new UserGradeNotFoundException(gradeName));

        return new ResponseUserGrade(
                findUserGrade.getGradeName().name(),
                findUserGrade.getRequiredMoney()
        );
    }

    @Override
    public void deleteUserGrade(String gradeName) {
        UserGrade findUserGrade = userGradeRepository.findById(UserGrade.Grade.valueOf(gradeName))
                .orElseThrow(() -> new UserGradeNotFoundException(gradeName));
        userGradeRepository.delete(findUserGrade);
    }

    @Override
    public List<ResponseUserGrade> getAllUserGrades() {
        List<UserGrade> userGrades = userGradeRepository.findAll();
        return userGrades.stream()
                .map(grade -> new ResponseUserGrade(
                        grade.getGradeName().name(),
                        grade.getRequiredMoney()))
                .toList();
    }
}
