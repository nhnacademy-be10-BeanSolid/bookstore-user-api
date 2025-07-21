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
class DormantUserSchedulerTest {

    @InjectMocks
    private DormantUserScheduler dormantUserScheduler;

    @Mock
    private UserService userService;

    @Test
    void testUpdateDormantUsers() {
        dormantUserScheduler.updateDormantUsers();
        verify(userService, times(1)).updateDormantUsers();
    }
}
