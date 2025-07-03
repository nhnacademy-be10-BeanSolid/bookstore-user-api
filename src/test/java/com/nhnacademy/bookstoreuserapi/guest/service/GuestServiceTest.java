package com.nhnacademy.bookstoreuserapi.guest.service;

import com.nhnacademy.bookstoreuserapi.guest.domain.GuestCreateRequest;
import com.nhnacademy.bookstoreuserapi.guest.domain.GuestUpdateRequest;
import com.nhnacademy.bookstoreuserapi.guest.domain.ResponseGuest;
import com.nhnacademy.bookstoreuserapi.guest.exception.GuestNotFoundException;
import com.nhnacademy.bookstoreuserapi.guest.service.impl.GuestServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql(scripts = "/guest-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
class GuestServiceTest {

    @Autowired
    GuestServiceImpl guestService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void testGetGuest_success() {
        ResponseGuest guest = guestService.getGuest("hong@test.com");

        assertThat(guest).isNotNull();
        assertThat(guest.getGuestName()).isEqualTo("홍길동");
        assertThat(guest.getGuestEmail()).isEqualTo("hong@test.com");
    }

    @Test
    void testGetGuest_notFound() {
        assertThrows(GuestNotFoundException.class,
                () -> guestService.getGuest("notfound@test.com"));
    }

    @Test
    void testAddGuest_success() {
        GuestCreateRequest request = new GuestCreateRequest(
                "myPassword", "장보고", "010-9999-9999", "청해진", "jang@test.com"
        );

        ResponseGuest response = guestService.addGuest(request);

        assertThat(response.getGuestEmail()).isEqualTo("jang@test.com");
        assertThat(passwordEncoder.matches("myPassword", response.getGuestPassword())).isTrue();
        assertThat(response.getGuestName()).isEqualTo("장보고");
    }

    @Test
    void testUpdateGuest_success() {
        GuestUpdateRequest updateRequest = new GuestUpdateRequest(
                "updatedPw", "홍길순", "010-0000-0000", "서울시"
        );

        ResponseGuest updated = guestService.updateGuest("hong@test.com", updateRequest);

        assertThat(passwordEncoder.matches("updatedPw", updated.getGuestPassword())).isTrue();
        assertThat(updated.getGuestName()).isEqualTo("홍길순");
        assertThat(updated.getGuestPhoneNumber()).isEqualTo("010-0000-0000");
    }

    @Test
    void testUpdateGuest_notFound() {
        GuestUpdateRequest updateRequest = new GuestUpdateRequest("pw", "이름", "010", "주소");

        assertThrows(GuestNotFoundException.class,
                () -> guestService.updateGuest("notfound@test.com", updateRequest));
    }

    @Test
    void testDeleteGuest_success() {
        guestService.deleteGuest("hong@test.com");

        assertThrows(GuestNotFoundException.class,
                () -> guestService.getGuest("hong@test.com"));
    }

    @Test
    void testDeleteGuest_notFound() {
        assertThrows(GuestNotFoundException.class,
                () -> guestService.deleteGuest("nonexistent@test.com"));
    }
}
