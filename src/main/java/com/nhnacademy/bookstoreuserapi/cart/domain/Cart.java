package com.nhnacademy.bookstoreuserapi.cart.domain;

import com.nhnacademy.bookstoreuserapi.common.entity.BaseTimeEntity;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "carts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_guest_uuid", columnNames = "guest_uuid")
        }
)
@Getter
@NoArgsConstructor
public class Cart extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", columnDefinition = "BIGINT UNSIGNED")
    private Long cartId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", foreignKey = @ForeignKey(name = "fk_cart_user"))
    private User user;

    @Setter
    @Column(name = "guest_uuid", unique = true, columnDefinition = "CHAR(36)")
    private String guestUUID;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(
            name = "owner_type",
            nullable = false,
            columnDefinition = "ENUM('USER', 'GUEST')"
    )
    private OwnerType ownerType;

    @BatchSize(size = 100)
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
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
        boolean itemExists = this.items.stream().anyMatch(i -> i.getItemId().equals(itemId));
        if (itemExists) {
            throw new DataIntegrityViolationException("Item with ID " + itemId + " already exists in the cart.");
        }
        CartItem item = new CartItem(itemId, quantity);
        item.setCart(this);
        this.items.add(item);
    }

    public void removeItem(Long itemId) {
        this.items.removeIf(i -> {
            boolean removed = i.getItemId().equals(itemId);
            if (removed) {
                i.setCart(null);
            }
            return removed;
        });
    }
}
