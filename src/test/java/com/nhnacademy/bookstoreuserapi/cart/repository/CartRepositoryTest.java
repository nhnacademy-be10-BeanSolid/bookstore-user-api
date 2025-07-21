package com.nhnacademy.bookstoreuserapi.cart.repository;

import com.nhnacademy.bookstoreuserapi.cart.domain.Cart;
import com.nhnacademy.bookstoreuserapi.cart.domain.OwnerType;
import com.nhnacademy.bookstoreuserapi.common.config.QuerydslConfig;
import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade;
import com.nhnacademy.bookstoreuserapi.usergrade.repository.UserGradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.nhnacademy.bookstoreuserapi.usergrade.domain.UserGrade.Grade.BASIC;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class CartRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserGradeRepository userGradeRepository;

    private final String userId = "testUser";

    @BeforeEach
    void setUp() {
        User user = new User(
                userId,
                "password",
                "홍길동",
                "01012345678",
                "hong@test.com",
                LocalDate.of(1995, 5, 1)
        );
        user.setUserStatus(User.Status.ACTIVE);
        user.setUserPoint(100);
        user.setCreatedAt(LocalDateTime.now().minusDays(1));
        UserGrade userGrade = new UserGrade(BASIC, 0L);

        userGradeRepository.save(userGrade);

        user.setUserGrade(userGrade);

        userRepository.save(user);
    }

    @Test
    @DisplayName("사용자 ID로 장바구니 존재 여부 확인")
    void testExistsByUser_UserId() {
        User user = userRepository.findByUserId(userId);

        Cart cart = new Cart(user);
        cartRepository.save(cart);

        boolean exists = cartRepository.existsByUser_UserId("testUser");
        assertThat(exists).isTrue();

        boolean notExists = cartRepository.existsByUser_UserId("nonExisting");
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("소유자 타입과 사용자 ID로 장바구니 찾기")
    void testFindByOwnerTypeAndUser_UserId() {

        User user = userRepository.findByUserId(userId);

        Cart cart = new Cart(user);
        cartRepository.save(cart);

        Optional<Cart> found = cartRepository.findByOwnerTypeAndUser_UserId(OwnerType.USER, "testUser");
        assertThat(found).isPresent();
        assertThat(found.get().getUser().getUserId()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("소유자 타입과 게스트 UUID로 장바구니 찾기")
    void testFindByOwnerTypeAndGuestUUID() {
        Cart cart = new Cart("guest-uuid-1234");
        cartRepository.save(cart);

        Optional<Cart> found = cartRepository.findByOwnerTypeAndGuestUUID(OwnerType.GUEST, "guest-uuid-1234");
        assertThat(found).isPresent();
        assertThat(found.get().getGuestUUID()).isEqualTo("guest-uuid-1234");
    }

    @Test
    @DisplayName("CreatedAt 이전 날짜 기준으로 USER 장바구니 삭제")
    void testDeleteByOwnerTypeAndCreatedAtBefore() {

        User user = userRepository.findByUserId(userId);

        Cart oldCart = new Cart(user);

        cartRepository.save(oldCart);

        cartRepository.deleteByOwnerTypeAndCreatedAtBefore(OwnerType.USER, LocalDateTime.now().plusDays(1));

        assertThat(cartRepository.existsByUser_UserId("testUser")).isFalse();
    }

    @Test
    @DisplayName("UpdatedAt 이전 날짜 기준으로 GUEST 장바구니 삭제")
    void testDeleteByOwnerTypeAndUpdatedAtBefore() {
        Cart oldCart = new Cart("delete-guest-uuid");
        cartRepository.save(oldCart);

        cartRepository.deleteByOwnerTypeAndUpdatedAtBefore(OwnerType.GUEST, LocalDateTime.now().plusDays(1));

        Optional<Cart> found = cartRepository.findByOwnerTypeAndGuestUUID(OwnerType.GUEST, "delete-guest-uuid");
        assertThat(found).isEmpty();
    }
}
