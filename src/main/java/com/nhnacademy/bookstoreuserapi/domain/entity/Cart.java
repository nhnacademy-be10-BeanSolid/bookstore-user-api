package com.nhnacademy.bookstoreuserapi.domain.entity;

import com.nhnacademy.bookstoreuserapi.domain.request.CartCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;

    @Column(nullable = false)
    private long bookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @Column(nullable = false)
    private int quantity;

    public Cart(CartCreateRequest cart, User user) {
        this.bookId = cart.bookId();
        this.user = user;
        this.quantity = cart.quantity();
    }
}
