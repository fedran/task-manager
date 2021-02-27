package org.fedran.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.fedran.manager.repository.UserRepository;
import org.fedran.manager.domain.User;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    private final UserService userService;
    private final EntityManagerFactory emf;
    private final UserRepository userRepository;

    private User user;

    @Autowired
    public UserServiceTest(
            UserService userService,
            EntityManagerFactory emf,
            UserRepository userRepository
    ) {
        this.userService = userService;
        this.emf = emf;
        this.userRepository = userRepository;
    }

    @Test
    public void createTest() {
        final var user = userService.create("test name");
        assertNotNull(user);
        assertNotNull(user.getUserId());
        assertEquals("test name", user.getName());
        deleteAll();
    }

    @Test
    public void findByNameLikeTest() {
        createUser();
        final var users = userService.findByNameLike("Alex");
        assertEquals(1, users.size());
        assertTrue(users.stream().anyMatch(u -> u.equals(user)));
        deleteAll();
    }

    @Test
    public void findAllUsersTest() {
        createUser();
        final var egor = userService.create("Egor");
        final var users = userService.findByNameLike("");
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.equals(user)));
        assertTrue(users.stream().anyMatch(u -> u.equals(egor)));
        deleteAll();
    }

    @Test
    public void deleteTest() {
        createUser();
        assertFalse(userService.findByNameLike(user.getName()).isEmpty());
        userService.delete("Alex");
        assertTrue(userService.findByNameLike("Alex").isEmpty());
        deleteAll();
    }

    private void createUser() {
        final var entityManager = this.emf.createEntityManager();
        entityManager.getTransaction().begin();

        user = userService.create("Alex");

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private void deleteAll() {
        userRepository.deleteAll();
    }
}
