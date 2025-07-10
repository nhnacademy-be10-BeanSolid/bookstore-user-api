package com.nhnacademy.bookstoreuserapi.usergrade.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_grades")
public class UserGrade {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "grade_name")
    private Grade gradeName;

    @Column(name = "required_money", nullable = false)
    private long requiredMoney;

    public UserGrade(UserGradeCreateRequest userGrade) {
        this.gradeName = Grade.valueOf(userGrade.gradeName());
        this.requiredMoney = userGrade.requiredMoney();
    }

    public UserGrade(Grade grade, long requiredMoney) {
        this.gradeName = grade;
        this.requiredMoney = requiredMoney;
    }

    public enum Grade {
        COMMON,
        BASIC,     //1퍼 적립혜택   1퍼
        ROYAL,      //+1퍼 적립혜택  2퍼
        GOLD,       //+1퍼 적립혜택  3퍼
        PLATINUM    //+1퍼 적립혜택  4퍼
    }
}
