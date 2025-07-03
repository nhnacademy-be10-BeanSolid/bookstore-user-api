package com.nhnacademy.bookstoreuserapi.guest.repository;

import com.nhnacademy.bookstoreuserapi.common.config.QuerydslConfig;
import com.nhnacademy.bookstoreuserapi.guest.domain.Guest;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import(QuerydslConfig.class)
class GuestRepositoryTest {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    EntityManager entityManager;

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

        Guest updated = guestRepository.findByGuestEmail("test@example.com");

        updated.setGuestPassword("newPassword");
        updated.setGuestName("새이름");
        updated.setGuestPhoneNumber("010-1111-2222");
        updated.setGuestAddress("신주소");

        entityManager.flush();
        entityManager.clear();

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