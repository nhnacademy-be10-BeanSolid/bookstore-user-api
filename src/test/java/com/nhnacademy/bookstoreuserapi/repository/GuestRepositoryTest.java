package com.nhnacademy.bookstoreuserapi.repository;

import com.nhnacademy.bookstoreuserapi.config.QuerydslConfig;
import com.nhnacademy.bookstoreuserapi.domain.entity.Guest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
@Import(QuerydslConfig.class)
class GuestRepositoryTest {

    @Autowired
    private GuestRepository guestRepository;

    @BeforeEach
    void setup() {
        Guest guest = new Guest();
        guest.setGuestEmail("test@example.com");
        guest.setGuestPassword("password");
        guest.setGuestName("홍길동");
        guest.setGuestPhoneNumber("010-1234-5678");
        guest.setGuestAddress("서울");

        guestRepository.save(guest);
    }

    @Test
    @DisplayName("이메일로 Guest 조회")
    void testFindByGuestEmail() {
        Guest found = guestRepository.findByGuestEmail("test@example.com");

        assertThat(found).isNotNull();
        assertThat(found.getGuestName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("Guest 정보 수정")
    void testUpdateGuest() {
        guestRepository.updateGuest(
                "newPassword",
                "새이름",
                "010-1111-2222",
                "신주소",
                "test@example.com"
        );

        Guest updated = guestRepository.findByGuestEmail("test@example.com");

        assertThat(updated.getGuestPassword()).isEqualTo("newPassword");
        assertThat(updated.getGuestName()).isEqualTo("새이름");
        assertThat(updated.getGuestPhoneNumber()).isEqualTo("010-1111-2222");
        assertThat(updated.getGuestAddress()).isEqualTo("신주소");
    }

    @Test
    @DisplayName("이메일로 Guest 삭제")
    void testDeleteByGuestEmail() {
        guestRepository.deleteByGuestEmail("test@example.com");

        Guest deleted = guestRepository.findByGuestEmail("test@example.com");
        assertThat(deleted).isNull();
    }
}