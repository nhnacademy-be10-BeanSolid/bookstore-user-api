package com.nhnacademy.bookstoreuserapi.user.scheduler;

import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.event.UserBirthEvent;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BirthdayEventPublisherTest {

    @InjectMocks
    private BirthdayEventPublisher birthdayEventPublisher;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void testPublishBirthdayEvents() {
        // given
        User user = mock(User.class);
        when(user.getUserNo()).thenReturn(1L);
        when(user.getUserBirth()).thenReturn(LocalDate.of(1990, 1, 1));
        // 오늘 날짜로 test, 실제 month/day는 별로 중요치 않음 (어차피 any month/day로 stub)
        when(userRepository.findByBirthMonthAndBirthDay(anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(user));

        // when
        birthdayEventPublisher.publishBirthdayEvents();

        // then
        verify(userRepository, times(1)).findByBirthMonthAndBirthDay(anyInt(), anyInt());
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq("birthday-exchange"), eq("birthday.user"), any(UserBirthEvent.class));
    }

    @Test
    void testPublishBirthdayEvents_whenNoUsers() {
        // 생일인 유저 없음
        when(userRepository.findByBirthMonthAndBirthDay(anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        birthdayEventPublisher.publishBirthdayEvents();

        verify(userRepository).findByBirthMonthAndBirthDay(anyInt(), anyInt());
        verifyNoInteractions(rabbitTemplate);
    }
}

