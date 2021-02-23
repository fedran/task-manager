package org.fedran.manager.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class TaskServiceTest {
    private final TaskService taskService;

    @Autowired
    private TaskServiceTest(TaskService taskService) {
        this.taskService = taskService;
    }

    @Test
    public void calculateRemainingTimeTest() {

    }
}