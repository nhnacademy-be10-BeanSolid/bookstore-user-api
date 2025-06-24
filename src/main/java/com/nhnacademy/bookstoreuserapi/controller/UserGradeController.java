package com.nhnacademy.bookstoreuserapi.controller;


import com.nhnacademy.bookstoreuserapi.controller.interfaces.UserGradeControllerDoc;
import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeUpdateRequest;
import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeCreateRequest;
import com.nhnacademy.bookstoreuserapi.domain.response.ResponseUserGrade;
import com.nhnacademy.bookstoreuserapi.exception.ValidationFailedException;
import com.nhnacademy.bookstoreuserapi.service.UserGradeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/grade")
public class UserGradeController implements UserGradeControllerDoc {
    private final UserGradeService userGradeService;

    @Override
    @PostMapping
    public ResponseEntity<ResponseUserGrade> addUserGrade(@Valid @RequestBody UserGradeCreateRequest userGrade, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userGradeService.saveUserGrade(userGrade));
    }

    @Override
    @PutMapping("/{gradeName}")
    public ResponseEntity<ResponseUserGrade> updateUserGrade(@PathVariable @NotBlank @Size(max = 10) String gradeName, @Valid @RequestBody UserGradeUpdateRequest userGrade, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        return ResponseEntity.ok().body(userGradeService.updateUserGrade(gradeName, userGrade));
    }


    @Override
    @GetMapping("/{gradeName}")
    public ResponseEntity<ResponseUserGrade> getUserGrade(@PathVariable @NotBlank @Size(max = 10) String gradeName) {
        return ResponseEntity.ok().body(userGradeService.getUserGrade(gradeName));
    }


    @Override
    @DeleteMapping("/{gradeName}")
    public ResponseEntity<Void> deleteUserGrade(@PathVariable @NotBlank @Size(max = 10) String gradeName) {
        userGradeService.deleteUserGrade(gradeName);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<ResponseUserGrade>> getAllUserGrades() {
        return ResponseEntity.ok().body(userGradeService.getAllUserGrades());
    }


}
