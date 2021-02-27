package org.fedran.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.fedran.manager.repository.ProjectRepository;
import org.springframework.test.context.ActiveProfiles;
import org.fedran.manager.repository.UserRepository;
import org.fedran.manager.domain.Project;
import org.fedran.manager.domain.User;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProjectServiceTest {
    private final ProjectService projectService;
    private final EntityManagerFactory entityManagerFactory;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private Project project;
    private User user;

    @Autowired
    public ProjectServiceTest(
            ProjectService projectService,
            EntityManagerFactory entityManagerFactory,
            ProjectRepository projectRepository,
            UserRepository userRepository
    ) {
        this.projectService = projectService;
        this.entityManagerFactory = entityManagerFactory;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Test
    public void createTest() {
        final var project = projectService.create("Project");
        assertNotNull(project);
        assertNotNull(project.getProjectId());
        deleteAll();
    }

    @Test
    public void findByNameTest() {
        createProject();
        final var project = projectService.findByName(this.project.getName());
        assertTrue(project.isPresent());
        assertEquals("One", project.get().getName());
        deleteAll();
    }

    @Test
    public void findAllTest() {
        createProject();
        projectService.create("Project two");
        final var projects = projectService.findALL();
        assertFalse(projects.isBlank());
        final var s = "Projects: \n" +
                          "One,\n" +
                          "Project two";
        assertEquals(s, projects);
        deleteAll();
    }

    @Test
    public void deleteByNameTest() {
        createProject();
        var one = projectService.findByName("One");
        assertTrue(one.isPresent());
        projectService.deleteByName("One");
        one = projectService.findByName("One");
        assertTrue(one.isEmpty());
        deleteAll();
    }

    @Test
    public void assignUserTest() {
        createProject();
        createUser();
        final var s = projectService.assignUser(user.getName(), project.getName());
        final var project = projectService.findByName(this.project.getName());
        assertTrue(project.isPresent());
        assertFalse(project.get().getUsers().isEmpty());
        assertEquals(user.getName(), project.get().getUsers().iterator().next().getName());
        assertEquals("Alex successfully assigned to One", s);
    }

    private void createUser() {
        final var entityManager = this.entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        user = new User();
        user.setName("Alex");
        entityManager.persist(user);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private void createProject() {
        final var entityManager = this.entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        project = new Project();
        project.setName("One");
        entityManager.persist(project);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private void deleteAll() {
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }
}
