package com.nhnacademy.bookstoreuserapi.user.scheduler;

import com.nhnacademy.bookstoreuserapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserGradeScheduler {

    private final UserService userService;

//    @Scheduled(cron = "0 0 1 * * *")
    @Scheduled(cron = "0 0 1 1 * *")
    public void scheduledBulkUpdateUserGrades() {
        userService.bulkUpdateUserGrades();
    }
}