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
                @UniqueConstraint(columnNames = {"userNo", "addressDetail"})
        }
)public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    @Column(nullable = false, length = 30)
    private String addressNickName;

    @Column(nullable = false)
    private String addressDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;
}
