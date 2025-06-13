package com.nhnacademy.bookstoreuserapi.controller;


import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUserGrade;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.UserGradeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/grade")
public class UserGradeController {
    private final UserGradeService userGradeService;

    @PostMapping
    public ResponseUserGrade addUserGrade(@Valid @RequestBody UserGradeCreateRequest userGrade, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return userGradeService.saveUserGrade(userGrade);
    }

    @PutMapping("/{gradeName}")
    public ResponseUserGrade updateUserGrade(@PathVariable @NotBlank @Size(max = 10) String gradeName, @Valid @RequestBody UserGradeUpdateRequest userGrade, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return userGradeService.updateUserGrade(gradeName, userGrade);
    }


    @GetMapping("/{gradeName}")
    public ResponseUserGrade getUserGrade(@PathVariable @NotBlank @Size(max = 10) String gradeName) {
        return userGradeService.getUserGrade(gradeName);
    }


    @DeleteMapping("/{gradeName}")
    public void deleteUserGrade(@PathVariable @NotBlank @Size(max = 10) String gradeName) {
        userGradeService.deleteUserGrade(gradeName);
    }

    @GetMapping("/all")
    public List<ResponseUserGrade> getAllUserGrades() {
        return userGradeService.getAllUserGrades();
    }


}
