package com.nhnacademy.bookstoreuserapi.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nhnacademy.bookstoreuserapi.address.domain.Address;
import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import com.nhnacademy.bookstoreuserapi.review.domain.Review;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "user_phone_number")
    private String userPhoneNumber;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_birth")
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

    public User(String provider, String providerId) {
        this.userId = provider + providerId;
        this.provider = provider;
        this.providerId = providerId;
    }
}
