package com.nhnacademy.bookstoreuserapi.service.impl;


import com.nhnacademy.bookstoreuserapi.domain.entity.UserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestUserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestUserGrade;
import com.nhnacademy.bookstoreuserapi.exception.InvalidDataException;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.repository.UserGradeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserGradeServiceImplTest {
    @Mock
    UserGradeRepository userGradeRepository;

    @InjectMocks
    UserGradeServiceImpl userGradeService;

    @Test
    void saveUserGrade() {
        SignUpRequestUserGrade signUpRequestUserGrade = new SignUpRequestUserGrade("BASIC", 0L);
        UserGrade userGrade = new UserGrade(signUpRequestUserGrade);
        Mockito.when(userGradeRepository.existsByGradeName(UserGrade.Grade.BASIC)).thenReturn(false);
        Mockito.when(userGradeRepository.save(userGrade)).thenReturn(userGrade);

        userGradeService.saveUserGrade(signUpRequestUserGrade);
        Mockito.verify(userGradeRepository, Mockito.times(1)).existsByGradeName(UserGrade.Grade.BASIC);
        Mockito.verify(userGradeRepository, Mockito.times(1)).save(userGrade);
    }

    @Test
    void saveUserGradeFail(){
        SignUpRequestUserGrade signUpRequestUserGrade = new SignUpRequestUserGrade("BASIC", 0L);

        Mockito.when(userGradeRepository.existsByGradeName(UserGrade.Grade.BASIC)).thenReturn(true);
        Assertions.assertThrows(UserGradeAlreadyExistException.class, () -> userGradeService.saveUserGrade(signUpRequestUserGrade));

        Mockito.verify(userGradeRepository, Mockito.times(1)).existsByGradeName(UserGrade.Grade.BASIC);

    }

    @Test
    void saveUserGradeFailNegativeMoney() {
        SignUpRequestUserGrade signUpRequestUserGrade = new SignUpRequestUserGrade("BASIC", -100L);
        Assertions.assertThrows(InvalidDataException.class, ()-> userGradeService.saveUserGrade(signUpRequestUserGrade));
    }

    @Test
    void updateUserGrade(){
        String gradeName = "BASIC";
        UserGrade existingUserGrade = new UserGrade(new SignUpRequestUserGrade(gradeName, 0L));

        Mockito.when(userGradeRepository.existsByGradeName(UserGrade.Grade.BASIC)).thenReturn(true);
        Mockito.when(userGradeRepository.findById(UserGrade.Grade.BASIC)).thenReturn(java.util.Optional.of(existingUserGrade));

        userGradeService.updateUserGrade(gradeName, new EditRequestUserGrade(gradeName, 10L));

        Mockito.verify(userGradeRepository, Mockito.times(1)).existsByGradeName(UserGrade.Grade.BASIC);
        Mockito.verify(userGradeRepository, Mockito.times(1)).findById(UserGrade.Grade.BASIC);
    }

    @Test
    void updateUserGradeFailNegativeMoney() {
        String gradeName = "BASIC";
        UserGrade existingUserGrade = new UserGrade(new SignUpRequestUserGrade(gradeName, 0L));

        Mockito.when(userGradeRepository.findById(UserGrade.Grade.BASIC)).thenReturn(java.util.Optional.of(existingUserGrade));
        EditRequestUserGrade editRequestUserGrade = new EditRequestUserGrade(gradeName, -100L);

        Assertions.assertThrows(InvalidDataException.class, () -> userGradeService.updateUserGrade(gradeName, editRequestUserGrade));
    }

    @Test
    void updateUserGradeFailAlreadyExists() {
        String gradeName = "BASIC";
        UserGrade existingUserGrade = new UserGrade(new SignUpRequestUserGrade(gradeName, 0L));

        Mockito.when(userGradeRepository.findById(UserGrade.Grade.BASIC)).thenReturn(java.util.Optional.of(existingUserGrade));
        Mockito.when(userGradeRepository.existsByGradeName(UserGrade.Grade.ROYAL)).thenReturn(true);

        EditRequestUserGrade editRequestUserGrade = new EditRequestUserGrade("ROYAL", 100L);

        Assertions.assertThrows(UserGradeAlreadyExistException.class, () -> userGradeService.updateUserGrade(gradeName, editRequestUserGrade));
    }

    @Test
    void getUserGrade() {
        String gradeName = "BASIC";
        UserGrade existingUserGrade = new UserGrade(new SignUpRequestUserGrade(gradeName, 0L));

        Mockito.when(userGradeRepository.findById(UserGrade.Grade.BASIC)).thenReturn(java.util.Optional.of(existingUserGrade));

        userGradeService.getUserGrade(gradeName);

        Mockito.verify(userGradeRepository, Mockito.times(1)).findById(UserGrade.Grade.BASIC);
    }

    @Test
    void deleteUserGrade() {
        String gradeName = "BASIC";
        UserGrade existingUserGrade = new UserGrade(new SignUpRequestUserGrade(gradeName, 0L));

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
