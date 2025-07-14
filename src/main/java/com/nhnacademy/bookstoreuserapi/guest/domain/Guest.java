package com.nhnacademy.bookstoreuserapi.guest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guests")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private Long guestId;

    @Column(name = "guest_password", nullable = false)
    private String guestPassword;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    public Guest(String encodedPassword, Long orderId) {
        this.guestPassword = encodedPassword;
        this.orderId = orderId;
    }
}
