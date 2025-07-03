package com.nhnacademy.bookstoreuserapi.usergrade.service;


import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGradeUpdateRequest;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGradeCreateRequest;
import com.nhnacademy.bookstoreuserapi.usergrade.exception.UserGradeAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import com.nhnacademy.bookstoreuserapi.usergrade.service.impl.UserGradeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserGradeServiceTest {
    @Mock
    UserGradeRepository userGradeRepository;

    @InjectMocks
    UserGradeServiceImpl userGradeService;

    @Test
    void saveUserGrade() {
        UserGradeCreateRequest userGradeCreateRequest = new UserGradeCreateRequest("BASIC", 0L);
        UserGrade userGrade = new UserGrade(userGradeCreateRequest);
        Mockito.when(userGradeRepository.existsByGradeName(UserGrade.Grade.BASIC)).thenReturn(false);
        Mockito.when(userGradeRepository.save(userGrade)).thenReturn(userGrade);

        userGradeService.saveUserGrade(userGradeCreateRequest);
        Mockito.verify(userGradeRepository, Mockito.times(1)).existsByGradeName(UserGrade.Grade.BASIC);
        Mockito.verify(userGradeRepository, Mockito.times(1)).save(userGrade);
    }

    @Test
    void saveUserGradeFail(){
        UserGradeCreateRequest userGradeCreateRequest = new UserGradeCreateRequest("BASIC", 0L);

        Mockito.when(userGradeRepository.existsByGradeName(UserGrade.Grade.BASIC)).thenReturn(true);
        Assertions.assertThrows(UserGradeAlreadyExistException.class, () -> userGradeService.saveUserGrade(userGradeCreateRequest));

        Mockito.verify(userGradeRepository, Mockito.times(1)).existsByGradeName(UserGrade.Grade.BASIC);

    }

    @Test
    void updateUserGrade(){
        String gradeName = "BASIC";
        UserGrade existingUserGrade = new UserGrade(new UserGradeCreateRequest(gradeName, 0L));

        Mockito.when(userGradeRepository.existsByGradeName(UserGrade.Grade.BASIC)).thenReturn(true);
        Mockito.when(userGradeRepository.findById(UserGrade.Grade.BASIC)).thenReturn(java.util.Optional.of(existingUserGrade));

        userGradeService.updateUserGrade(gradeName, new UserGradeUpdateRequest(gradeName, 10L));

        Mockito.verify(userGradeRepository, Mockito.times(1)).existsByGradeName(UserGrade.Grade.BASIC);
        Mockito.verify(userGradeRepository, Mockito.times(1)).findById(UserGrade.Grade.BASIC);
    }

    @Test
    void updateUserGradeFailAlreadyExists() {
        String gradeName = "BASIC";
        UserGrade existingUserGrade = new UserGrade(new UserGradeCreateRequest(gradeName, 0L));

        Mockito.when(userGradeRepository.findById(UserGrade.Grade.BASIC)).thenReturn(java.util.Optional.of(existingUserGrade));
        Mockito.when(userGradeRepository.existsByGradeName(UserGrade.Grade.ROYAL)).thenReturn(true);

        UserGradeUpdateRequest userGradeUpdateRequest = new UserGradeUpdateRequest("ROYAL", 100L);

        Assertions.assertThrows(UserGradeAlreadyExistException.class, () -> userGradeService.updateUserGrade(gradeName, userGradeUpdateRequest));
    }

    @Test
    void getUserGrade() {
        String gradeName = "BASIC";
        UserGrade existingUserGrade = new UserGrade(new UserGradeCreateRequest(gradeName, 0L));

        Mockito.when(userGradeRepository.findById(UserGrade.Grade.BASIC)).thenReturn(java.util.Optional.of(existingUserGrade));

        userGradeService.getUserGrade(gradeName);

        Mockito.verify(userGradeRepository, Mockito.times(1)).findById(UserGrade.Grade.BASIC);
    }

    @Test
    void deleteUserGrade() {
        String gradeName = "BASIC";
        UserGrade existingUserGrade = new UserGrade(new UserGradeCreateRequest(gradeName, 0L));

        Mockito.when(userGradeRepository.findById(UserGrade.Grade.BASIC)).thenReturn(java.util.Optional.of(existingUserGrade));

        userGradeService.deleteUserGrade(gradeName);

        Mockito.verify(userGradeRepository, Mockito.times(1)).findById(UserGrade.Grade.BASIC);
        Mockito.verify(userGradeRepository, Mockito.times(1)).delete(existingUserGrade);
    }

    @Test
    void getAllUserGrades() {
        Mockito.when(userGradeRepository.findAll()).thenReturn(List.of(new UserGrade(UserGrade.Grade.BASIC, 0L),
                new UserGrade(UserGrade.Grade.ROYAL, 100L)));
        userGradeService.getAllUserGrades();
        Mockito.verify(userGradeRepository, Mockito.times(1)).findAll();
    }
}
