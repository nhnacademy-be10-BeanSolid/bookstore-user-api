package com.nhnacademy.bookstoreuserapi.guest.service;

import com.nhnacademy.bookstoreuserapi.guest.domain.*;
import com.nhnacademy.bookstoreuserapi.guest.exception.GuestNotFoundException;
import com.nhnacademy.bookstoreuserapi.guest.repository.GuestRepository;
import com.nhnacademy.bookstoreuserapi.guest.service.impl.GuestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GuestServiceTest {

    @InjectMocks
    private GuestServiceImpl guestService;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).then(invocation -> {
            String raw = invocation.getArgument(0);
            String encoded = invocation.getArgument(1);
            // 단순히 encodedPassword로만 매칭 처리
            return "encodedPassword".equals(encoded);
        });
    }

    private Guest createGuest(String email, String name, String password) {
        Guest guest = new Guest();
        guest.setGuestId(1L);
        guest.setGuestEmail(email);
        guest.setGuestName(name);
        guest.setGuestPassword(password);
        guest.setGuestPhoneNumber("010-1234-5678");
        guest.setGuestAddress("서울시");
        return guest;
    }

    @Test
    @DisplayName("게스트 조회 성공")
    void testGetGuest_success() {
        Guest guest = createGuest("hong@test.com", "홍길동", "encodedPassword");
        when(guestRepository.findByGuestEmail("hong@test.com")).thenReturn(guest);

        ResponseGuest response = guestService.getGuest("hong@test.com");

        assertThat(response).isNotNull();
        assertThat(response.getGuestName()).isEqualTo("홍길동");
        assertThat(response.getGuestEmail()).isEqualTo("hong@test.com");
    }

    @Test
    @DisplayName("게스트 조회 실패")
    void testGetGuest_notFound() {
        when(guestRepository.findByGuestEmail("notfound@test.com")).thenReturn(null);

        assertThrows(GuestNotFoundException.class,
                () -> guestService.getGuest("notfound@test.com"));
    }

    @Test
    @DisplayName("게스트 등록 성공")
    void testAddGuest_success() {
        GuestCreateRequest request = new GuestCreateRequest(
                "myPassword", "장보고", "010-9999-9999", "청해진", "jang@test.com"
        );
        Guest guest = new Guest(request);
        guest.setGuestPassword("encodedPassword");
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);

        ResponseGuest response = guestService.addGuest(request);

        assertThat(response.getGuestEmail()).isEqualTo("jang@test.com");
        assertThat(passwordEncoder.matches("myPassword", response.getGuestPassword())).isTrue();
        assertThat(response.getGuestName()).isEqualTo("장보고");
    }

    @Test
    @DisplayName("게스트 정보 수정 성공")
    void testUpdateGuest_success() {
        GuestUpdateRequest updateRequest = new GuestUpdateRequest(
                "updatedPw", "홍길순", "010-0000-0000", "서울시"
        );
        Guest guest = createGuest("hong@test.com", "홍길동", "oldPassword");
        when(guestRepository.findByGuestEmail("hong@test.com")).thenReturn(guest);

        ResponseGuest updated = guestService.updateGuest("hong@test.com", updateRequest);

        assertThat(passwordEncoder.matches("updatedPw", updated.getGuestPassword())).isTrue();
        assertThat(updated.getGuestName()).isEqualTo("홍길순");
        assertThat(updated.getGuestPhoneNumber()).isEqualTo("010-0000-0000");
    }

    @Test
    @DisplayName("게스트 정보 수정 실패")
    void testUpdateGuest_notFound() {
        GuestUpdateRequest updateRequest = new GuestUpdateRequest("pw", "이름", "010", "주소");
        when(guestRepository.findByGuestEmail("notfound@test.com")).thenReturn(null);

        assertThrows(GuestNotFoundException.class,
                () -> guestService.updateGuest("notfound@test.com", updateRequest));
    }

    @Test
    @DisplayName("게스트 삭제 성공")
    void testDeleteGuest_success() {
        Guest guest = createGuest("hong@test.com", "홍길동", "encodedPassword");
        when(guestRepository.findByGuestEmail("hong@test.com")).thenReturn(guest);

        guestService.deleteGuest("hong@test.com");

        verify(guestRepository).deleteByGuestEmail("hong@test.com");
    }

    @Test
    @DisplayName("게스트 삭제 실패")
    void testDeleteGuest_notFound() {
        when(guestRepository.findByGuestEmail("nonexistent@test.com")).thenReturn(null);

        assertThrows(GuestNotFoundException.class,
                () -> guestService.deleteGuest("nonexistent@test.com"));
    }
}
