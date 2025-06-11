package com.nhnacademy.bookstoreuserapi.controller;


import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestUserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestUserGrade;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUserGrade;
import com.nhnacademy.bookstoreuserapi.service.impl.UserGradeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/grade")
public class UserGradeController {
    private final UserGradeServiceImpl userGradeService;

    @PostMapping
    public ResponseUserGrade addUserGrade(@RequestBody SignUpRequestUserGrade userGrade) {
        return userGradeService.saveUserGrade(userGrade);
    }

    @PutMapping("/{gradeName}")
    public ResponseUserGrade updateUserGrade(@PathVariable String gradeName, @RequestBody EditRequestUserGrade userGrade) {
        return userGradeService.updateUserGrade(gradeName, userGrade);
    }

    @GetMapping("/{gradeName}")
    public ResponseUserGrade getUserGrade(@PathVariable String gradeName) {
        return userGradeService.getUserGrade(gradeName);
    }

    @DeleteMapping("/{gradeName}")
    public void deleteUserGrade(@PathVariable String gradeName) {
        userGradeService.deleteUserGrade(gradeName);
    }

    @GetMapping("/all")
    public List<ResponseUserGrade> getAllUserGrades() {
        return userGradeService.getAllUserGrades();
    }


}
