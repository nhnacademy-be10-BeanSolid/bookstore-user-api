package com.nhnacademy.bookstoreuserapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreuserapi.domain.request.EditRequestUserGrade;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestUserGrade;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeAlreadyExistException;
import com.nhnacademy.bookstoreuserapi.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreuserapi.service.impl.UserGradeServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserGradeController.class)
@AutoConfigureMockMvc
class UserGradeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserGradeServiceImpl userGradeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addUserGrade() throws Exception {
        SignUpRequestUserGrade userGrade = new SignUpRequestUserGrade("BASIC", 0);
        Mockito.when(userGradeService.saveUserGrade(userGrade)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGrade)))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(userGradeService, Mockito.times(1)).saveUserGrade(userGrade);
    }

    @Test
    void addUserGradeFailExists() throws Exception {
        SignUpRequestUserGrade userGrade = new SignUpRequestUserGrade("BASIC", 0);
        Mockito.when(userGradeService.saveUserGrade(userGrade)).thenThrow(new UserGradeAlreadyExistException(userGrade.getGradeName()));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGrade)))
                .andExpect(status().isConflict());
        Mockito.verify(userGradeService, Mockito.times(1)).saveUserGrade(userGrade);
    }

    @Test
    void getUserGrade() throws Exception {
        String grade = "BASIC";
        Mockito.when(userGradeService.getUserGrade(grade)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/grade/{gradeName}", grade)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(userGradeService, Mockito.times(1)).getUserGrade(grade);
    }

    @Test
    void getUserGradeNotFound() throws Exception {
        String grade = "BASIC";
        Mockito.when(userGradeService.getUserGrade(grade)).thenThrow(new UserGradeNotFoundException(grade));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/grade/{gradeName}", grade)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        Mockito.verify(userGradeService, Mockito.times(1)).getUserGrade(grade);
    }

    @Test
    void updateUserGrade() throws Exception {
        String grade = "BASIC";
        EditRequestUserGrade userGrade = new EditRequestUserGrade("ROYAL", 100);
        Mockito.when(userGradeService.updateUserGrade(grade, userGrade)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/grade/{gradeName}", grade)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGrade)))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(userGradeService, Mockito.times(1)).updateUserGrade(grade, userGrade);
    }

    @Test
    void deleteUserGrade() throws Exception {
        String grade = "BASIC";
        Mockito.doNothing().when(userGradeService).deleteUserGrade(grade);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/grade/{gradeName}", grade))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(userGradeService, Mockito.times(1)).deleteUserGrade(grade);
    }

    @Test
    void getAllUserGrades() throws Exception {
        Mockito.when(userGradeService.getAllUserGrades()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/grade/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        Mockito.verify(userGradeService, Mockito.times(1)).getAllUserGrades();
    }
}
