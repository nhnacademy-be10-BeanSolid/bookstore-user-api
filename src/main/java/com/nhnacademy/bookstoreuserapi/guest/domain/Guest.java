package com.nhnacademy.bookstoreuserapi.guest.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guests")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private Long guestId;

    @Column(name = "guest_password", nullable = false)
    private String guestPassword;

    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @Column(name = "guest_phone_number", nullable = false)
    private String guestPhoneNumber;

    @Column(name = "guest_address", nullable = false)
    private String guestAddress;

    @Column(name = "guest_email", nullable = false, unique = true)
    private String guestEmail;

    public Guest(GuestCreateRequest guestCreateRequest) {

        this.guestPassword = guestCreateRequest.guestPassword();
        this.guestName = guestCreateRequest.guestName();
        this.guestPhoneNumber = guestCreateRequest.guestPhoneNumber();
        this.guestAddress = guestCreateRequest.guestAddress();
        this.guestEmail = guestCreateRequest.guestEmail();
    }
}
