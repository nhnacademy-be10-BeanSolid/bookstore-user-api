package com.nhnacademy.bookstoreuserapi.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nhnacademy.bookstoreuserapi.domain.request.UserGradeCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_grade")
public class UserGrade {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "grade_name")
    private Grade gradeName;

    @Column(name = "required_money", nullable = false)
    private long requiredMoney;

    @OneToMany(mappedBy = "userGrade")
    @JsonIgnore
    private List<User> user;

    public UserGrade(UserGradeCreateRequest userGrade) {
        this.gradeName = Grade.valueOf(userGrade.getGradeName());
        this.requiredMoney = userGrade.getRequiredMoney();
    }

    public UserGrade(Grade grade, long requiredMoney) {
        this.gradeName = grade;
        this.requiredMoney = requiredMoney;
    }

    public enum Grade {
        BASIC,     //1퍼 적립혜택   1퍼
        ROYAL,      //+1퍼 적립혜택  2퍼
        GOLD,       //+1퍼 적립혜택  3퍼
        PLATINUM    //+1퍼 적립혜택  4퍼
    }
}
