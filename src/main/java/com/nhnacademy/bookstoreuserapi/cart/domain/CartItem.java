package com.nhnacademy.bookstoreuserapi.cart.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    private Long itemId;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @PrePersist @PreUpdate
    private void touchParent() {
        if(cart != null) {
            cart.setUpdatedAt(LocalDateTime.now());
        }
    }

    public CartItem(Long itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
