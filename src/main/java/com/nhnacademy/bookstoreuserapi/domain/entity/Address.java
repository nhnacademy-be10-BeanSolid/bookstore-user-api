package com.nhnacademy.bookstoreuserapi.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(
        name = "address",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "addressDetail"})
        }
)public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    @Column(nullable = false, length = 30)
    private String addressNickName;

    @Column(nullable = false)
    private String addressDetail;

//외래키 설정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
    private String userId;
}
