package com.nhnacademy.bookstoreuserapi.user.scheduler;

import com.nhnacademy.bookstoreuserapi.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserGradeSchedulerTest {

    @InjectMocks
    private UserGradeScheduler userGradeScheduler;

    @Mock
    private UserService userService;

    @Test
    void testUpdateUserGrades() {
        userGradeScheduler.scheduledBulkUpdateUserGrades();
        verify(userService, times(1)).bulkUpdateUserGrades();
    }
}
