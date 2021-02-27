package org.fedran.manager.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.BeforeEach;
import org.fedran.manager.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    private final UserService userService;
    private User user;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        user = userService.create("Alex");
    }

    @Test
    public void createTest() {
        final var user = userService.create("test name");
        assertNotNull(user);
        assertNotNull(user.getUserId());
        assertEquals("test name", user.getName());
    }

    @Test
    public void findByNameLikeTest() {
        final var users = userService.findByNameLike("Alex");
        assertTrue(users.stream().anyMatch(u -> u.equals(user)));
    }

    @Test
    public void findAllUsersTest() {
        final var egor = userService.create("Egor");
        final var users = userService.findByNameLike("");
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.equals(user)));
        assertTrue(users.stream().anyMatch(u -> u.equals(egor)));
    }

    @Test
    public void deleteTest() {
        userService.delete("Alex");
        assertTrue(userService.findByNameLike("Alex").isEmpty());
    }
}
