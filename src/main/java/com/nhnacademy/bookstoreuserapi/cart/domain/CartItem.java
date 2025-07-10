package com.nhnacademy.bookstoreuserapi.cart.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_cartitem_cart_book",
                        columnNames = {"cart_id", "item_id"}
                )
        }
)
@Check(constraints = "quantity > 0")
@Getter
@Setter
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id", columnDefinition = "BIGINT UNSIGNED")
    private Long cartItemId;

    private Long itemId;

    @Min(1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "cart_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cartitem_cart")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cart cart;

    @PrePersist @PreUpdate @PreRemove
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
