package com.nhnacademy.bookstoreuserapi.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nhnacademy.bookstoreuserapi.domain.request.SignUpRequestUserGrade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_grades")
public class UserGrade {
    @Id
    @Enumerated(EnumType.STRING)
    private Grade gradeName;

    @Column(nullable = false)
    private long requiredMoney;

    @OneToMany(mappedBy = "userGrade")
    @JsonIgnore
    private List<User> user;

    public UserGrade(SignUpRequestUserGrade userGrade) {
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
