package com.nhnacademy.bookstoreuserapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nhnacademy.bookstoreuserapi.domain.request.UserCreateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {

    public enum Status {
        ACTIVE,
        DORMANT,
        WITHDRAWN
    }

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_phone_number", nullable = false)
    private String userPhoneNumber;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_birth", nullable = false)
    private LocalDate userBirth;

    @Column(name = "user_point", nullable = false)
    private int userPoint;

    @Column(name = "is_auth", nullable = false)
    private boolean isAuth;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private Status userStatus;

    @Column(name = "last_login_at", nullable = false)
    private LocalDateTime lastLoginAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Address> address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Cart> carts;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_name", nullable = false)
    private UserGrade userGrade;

    public User(String userId, String userPassword, String userName, String userPhoneNumber, String userEmail, LocalDate userBirth) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userBirth = userBirth;
    }
}
