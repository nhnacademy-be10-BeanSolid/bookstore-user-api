package com.nhnacademy.bookstoreuserapi.user.scheduler;

import com.nhnacademy.bookstoreuserapi.user.domain.User;
import com.nhnacademy.bookstoreuserapi.user.event.UserBirthEvent;
import com.nhnacademy.bookstoreuserapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BirthdayEventPublisher {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 60000) // 매분 실행
    public void publishBirthdayEvents() {
        log.info("Birthday event publisher started.");

        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        List<User> birthdayUsers = userRepository.findByBirthMonthAndBirthDay(month, day);

        if (birthdayUsers.isEmpty()) {
            log.info("No birthday users found for today: {}-{}", month, day);
            return;
        }

        log.info("Found {} birthday users for today: {}-{}", birthdayUsers.size(), month, day);

        for (User user : birthdayUsers) {
            UserBirthEvent event = new UserBirthEvent(user.getUserNo(), user.getUserBirth());
            rabbitTemplate.convertAndSend("birthday-exchange", "birthday.user", event);
            log.info("Published birthday event for user: {}", user.getUserNo());
        }
        log.info("Birthday event publisher finished.");
    }
}
