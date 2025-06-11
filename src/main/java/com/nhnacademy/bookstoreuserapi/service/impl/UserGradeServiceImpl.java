package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestUserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestUserGrade;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUserGrade;
import com.nhnacademy.bookstoreuserapi.exception.InvalidDataException;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreuserapi.repository.UserGradeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserGradeServiceImpl {
    private final UserGradeRepository userGradeRepository;

    public ResponseUserGrade saveUserGrade(SignUpRequestUserGrade userGrade) {
        if (userGrade.getRequiredMoney() < 0) {
            throw new InvalidDataException("금액은 음수일 수 없습니다.");
        }
        if(userGradeRepository.existsByGradeName(UserGrade.Grade.valueOf(userGrade.getGradeName()))){
            throw new UserGradeAlreadyExistException(userGrade.getGradeName());
        }
        UserGrade savedUserGrade = userGradeRepository.save(new UserGrade(userGrade));
        return new ResponseUserGrade(
                savedUserGrade.getGradeName().name(),
                savedUserGrade.getRequiredMoney()
        );
    }

    public ResponseUserGrade updateUserGrade(String gradeName, EditRequestUserGrade userGrade) {
        UserGrade findUserGrade = userGradeRepository.findById(UserGrade.Grade.valueOf(gradeName))
                .orElseThrow(() -> new UserGradeNotFoundException(gradeName));

        if (userGrade.getRequiredMoney() < 0) {
            throw new InvalidDataException("금액은 음수일 수 없습니다.");
        }
        if(userGradeRepository.existsByGradeName(UserGrade.Grade.valueOf(userGrade.getGradeName())) &&
                !findUserGrade.getGradeName().name().equals(userGrade.getGradeName())){
            throw new UserGradeAlreadyExistException(userGrade.getGradeName());
        }

        findUserGrade.setGradeName(UserGrade.Grade.valueOf(userGrade.getGradeName()));
        findUserGrade.setRequiredMoney(userGrade.getRequiredMoney());

        log.warn("주의: 등급 {}의 금액이 위계상 비정상일 수 있음. (입력값: {})", userGrade.getGradeName(), userGrade.getRequiredMoney());

        return new ResponseUserGrade(
                findUserGrade.getGradeName().name(),
                findUserGrade.getRequiredMoney()
        );
    }

    public ResponseUserGrade getUserGrade(String gradeName) {
        UserGrade findUserGrade = userGradeRepository.findById(UserGrade.Grade.valueOf(gradeName))
                .orElseThrow(() -> new UserGradeNotFoundException(gradeName));

        return new ResponseUserGrade(
                findUserGrade.getGradeName().name(),
                findUserGrade.getRequiredMoney()
        );
    }

    public void deleteUserGrade(String gradeName) {
        UserGrade findUserGrade = userGradeRepository.findById(UserGrade.Grade.valueOf(gradeName))
                .orElseThrow(() -> new UserGradeNotFoundException(gradeName));
        userGradeRepository.delete(findUserGrade);
    }

    public List<ResponseUserGrade> getAllUserGrades() {
        List<UserGrade> userGrades = userGradeRepository.findAll();
        return userGrades.stream()
                .map(grade -> new ResponseUserGrade(
                        grade.getGradeName().name(),
                        grade.getRequiredMoney()))
                .toList();
    }
}
