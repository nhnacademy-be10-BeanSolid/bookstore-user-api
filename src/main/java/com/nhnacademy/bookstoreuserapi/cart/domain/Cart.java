package com.nhnacademy.bookstoreuserapi.cart.domain;

import com.nhnacademy.bookstoreuserapi.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @Column(columnDefinition = "CHAR(36)")
    private String guest_uuid;

    @Column(
            name = "owner_type",
            columnDefinition = "ENUM('USER', 'GUEST') NOT NULL"
    )
    private OwnerType ownerType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Cart(User user) {
        this.user = user;
        this.ownerType = OwnerType.USER;
    }

    public Cart(String guest_uuid) {
        this.guest_uuid = guest_uuid;
        this.ownerType = OwnerType.GUEST;
    }
}
