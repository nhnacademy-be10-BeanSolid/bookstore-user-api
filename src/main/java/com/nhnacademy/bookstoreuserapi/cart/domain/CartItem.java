package com.nhnacademy.bookstoreuserapi.cart.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public CartItem(Long itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
