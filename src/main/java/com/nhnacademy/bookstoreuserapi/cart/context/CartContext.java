package com.nhnacademy.bookstoreuserapi.cart.context;

import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestHeader;

@Getter
public class CartContext {
    private final OwnerType ownerType;
    private final String userId;
    private final String guestUUID;

    public CartContext(
            @RequestHeader("X-OWNER-TYPE") OwnerType ownerType,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-GUEST-UUID", required = false) String guestUUID
    ) {
        this.ownerType = ownerType;
        this.userId = userId;
        this.guestUUID = guestUUID;
    }

    public boolean isUser() {
        return ownerType == OwnerType.USER;
    }

    public String getIdentifier() {
        return isUser() ? userId : guestUUID;
    }


}
