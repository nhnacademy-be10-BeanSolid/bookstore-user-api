package com.nhnacademy.bookstoreuserapi.cart.domain;

import com.nhnacademy.bookstoreuserapi.common.entity.BaseTimeEntity;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "carts")
public class Cart extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @Column(name = "guest_uuid", unique = true, columnDefinition = "CHAR(36)")
    private String guestUUID;

    @Column(
            name = "owner_type",
            columnDefinition = "ENUM('USER', 'GUEST') NOT NULL"
    )
    private OwnerType ownerType;

    @BatchSize(size = 100)
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "cart_id")
    private List<CartItem> items = new ArrayList<>();

    public Cart(User user) {
        this.user = user;
        this.ownerType = OwnerType.USER;
    }

    public Cart(String guestUUID) {
        this.guestUUID = guestUUID;
        this.ownerType = OwnerType.GUEST;
    }

    public void addItem(Long itemId, int quantity) {
        CartItem item = new CartItem(itemId, quantity);
        item.setCart(this);
        this.items.add(item);
    }

    public void removeItem(Long itemId) {
        this.items.removeIf(i -> i.getItemId().equals(itemId));
    }
}
