package org.fedran.manager.service;

import org.fedran.manager.domain.Project;
import org.fedran.manager.domain.User;
import org.fedran.manager.repository.ProjectRepository;
import org.fedran.manager.repository.TaskRepository;
import org.fedran.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.fedran.manager.domain.Task;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TaskServiceTest {
    private final TaskService taskService;
    private final EntityManagerFactory entityManagerFactory;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    private Project project;
    private User user;
    private Task task;
    private Task subtask;

    @Autowired
    private TaskServiceTest(TaskService taskService, EntityManagerFactory entityManagerFactory, TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskService = taskService;
        this.entityManagerFactory = entityManagerFactory;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Test
    public void createTest() {
        final var task2 = taskService.create("Buy meal");
        assertNotNull(task2.getTaskId());
        deleteAll();
    }

    @Test
    public void findByNameTest() {
        generateTask();
        final var task = taskService.findByName("Homework");
        assertTrue(task.isPresent());
        assertEquals(100, task.get().getEstimateMin());
        assertEquals(50, task.get().getSpendMin());
        deleteAll();
    }

    @Test
    public void findAllTest() {
        generateSubtask();
        final var tasks = taskService.findAll();
        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(s -> s.equals("Task - Homework")));
        assertTrue(tasks.stream().anyMatch(s -> s.equals("Task - Subtask")));
        deleteAll();
    }

    @Test
    public void deleteByNameTest() {
        generateTask();
        final var task = taskService.findByName("Homework");
        assertTrue(task.isPresent());
        assertEquals("Homework", task.get().getName());
        taskService.deleteByName("Homework");
        assertTrue(taskService.findByName("Homework").isEmpty());
        deleteAll();
    }

    @Test
    public void assignTaskOnUserTest() {
        generateTaskAndUser();
        final var s = taskService.assignTaskOnUser("Homework", "Alex");
        final var task = taskService.findByName("Homework");
        assertTrue(task.isPresent());
        assertEquals(user, task.get().getAssignee());
        assertEquals("Homework assigned on Alex", s);
        deleteAll();
    }

    @Test
    public void addTaskToProjectTest() {
        setUp();
        final var s = taskService.addTaskToProject("Homework", "Home");
        final var task = taskService.findByName("Homework");
        assertTrue(task.isPresent());
        assertEquals(project, task.get().getProject());
        assertEquals("Homework successfully added to Home", s);
        deleteAll();
    }

    @Test
    public void generateReportTest() {
        setUp();
        final var report = taskService.generateReport("Home", "Alex");
        assertEquals("Tasks created for Home by Alex:" + System.lineSeparator() + "Homework", report);
        deleteAll();
    }

    @Test
    public void addSubtaskTest() {
        generateSubtask();
        taskService.addSubtask(task.getName(), this.subtask.getName());
        final var subtask = taskService.findByName(this.subtask.getName());
        assertTrue(subtask.isPresent());
        assertEquals(task, subtask.get().getParent());
        deleteAll();
    }

    @Test
    public void removeParentTest() {
        generateRelatedTasks();
        var subtask = taskService.findByName(this.subtask.getName());
        assertTrue(subtask.isPresent());
        assertNotNull(subtask.get().getParent());
        taskService.removeParent(this.subtask.getName());
        subtask = taskService.findByName(this.subtask.getName());
        assertTrue(subtask.isPresent());
        assertNull(subtask.get().getParent());
        deleteAll();
    }

    @Test
    public void closeTaskTest() {
        generateRelatedTasks();
        var task = taskService.findByName(this.task.getName());
        var subtask = taskService.findByName(this.subtask.getName());
        assertTrue(task.isPresent());
        assertTrue(subtask.isPresent());
        assertEquals(Task.Status.OPEN, task.get().getStatus());
        assertEquals(Task.Status.OPEN, subtask.get().getStatus());
        taskService.closeTask(this.task.getName());
        task = taskService.findByName(this.task.getName());
        subtask = taskService.findByName(this.subtask.getName());
        assertTrue(task.isPresent());
        assertTrue(subtask.isPresent());
        assertEquals(Task.Status.CLOSE, task.get().getStatus());
        assertEquals(Task.Status.CLOSE, subtask.get().getStatus());
        deleteAll();
    }

    @Test
    public void calculateRemainingTimeTest() {
        generateRelatedTasks();
        assertEquals("remaining time: 75", taskService.calculateRemainingTimeSum("Homework"));
        deleteAll();
    }


    public void deleteAll() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        projectRepository.deleteAll();
    }

    private void setUp() {
        final var entityManager = this.entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        user = new User();
        user.setName("Alex");
        entityManager.persist(user);

        project = new Project();
        project.setName("Home");
        entityManager.persist(project);

        task = new Task();
        task.setName("Homework");
        task.setEstimateMin(100);
        task.setSpendMin(50);
        task.setProject(project);
        task.setAssignee(user);
        entityManager.persist(task);

        subtask = new Task();
        subtask.setName("Subtask");
        subtask.setParent(task);
        entityManager.persist(subtask);

        entityManager.getTransaction().commit();
    }

    private void generateTask() {
        final var entityManager = this.entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        task = new Task();
        task.setName("Homework");
        entityManager.persist(task);
        task.setEstimateMin(100);
        task.setSpendMin(50);

        entityManager.getTransaction().commit();
    }

    private void generateRelatedTasks() {
        final var entityManager = this.entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        task = new Task();
        task.setName("Homework");
        task.setEstimateMin(100);
        task.setSpendMin(50);
        entityManager.persist(task);

        subtask = new Task();
        subtask.setName("Subtask");
        subtask.setParent(task);
        subtask.setEstimateMin(50);
        subtask.setSpendMin(25);
        entityManager.persist(subtask);

        entityManager.getTransaction().commit();
    }


    private void generateTaskAndUser() {
        final var entityManager = this.entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        user = new User();
        user.setName("Alex");
        entityManager.persist(user);

        task = new Task();
        task.setName("Homework");
        task.setEstimateMin(100);
        task.setSpendMin(50);
        task.setAssignee(user);
        entityManager.persist(task);

        entityManager.getTransaction().commit();
    }


    private void generateSubtask() {
        final var entityManager = this.entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        task = new Task();
        task.setName("Homework");
        task.setEstimateMin(100);
        task.setSpendMin(50);
        task.setProject(project);
        task.setAssignee(user);
        entityManager.persist(task);

        subtask = new Task();
        subtask.setName("Subtask");
        entityManager.persist(subtask);

        entityManager.getTransaction().commit();
    }
}
