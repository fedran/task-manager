package org.fedran.manager;

import org.fedran.manager.domain.Project;
import org.fedran.manager.domain.Task;
import org.fedran.manager.domain.User;
import org.fedran.manager.repository.ProjectRepository;
import org.fedran.manager.repository.TaskRepository;
import org.fedran.manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.shell.jline.InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class FirstTaskRepositoryTest {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public FirstTaskRepositoryTest(UserRepository userRepository, ProjectRepository projectRepository,
                                   TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }


    @Test
    void loadStoreTest() {
        var user = new User();
        user.setName("vasya");

        userRepository.save(user);

        var actual = userRepository.findByName("vasya").orElseThrow();
        assertEquals(user, actual);
    }

    @Test
    void assignUserOnProject() {
        var user = new User();
        user.setName("vasya");
        userRepository.save(user);

        var project = new Project();
        project.setName("vasya's project");
        projectRepository.save(project);

        project.addUser(user);
        projectRepository.save(project);

        var actual = userRepository.findByName("vasya").orElseThrow();

        assertEquals(1, actual.getProjects().size());
        var first = actual.getProjects().stream().findFirst().orElseThrow();
        assertEquals(project, first);

        var actualProject = projectRepository.findByName("vasya's project").orElseThrow();

        assertEquals(1, actualProject.getUsers().size());
        var firstProject = actualProject.getUsers().stream().findFirst().orElseThrow();
        assertEquals(user, firstProject);
    }

    @Test
    void assignTaskOnUser() {
        var user = new User();
        user.setName("vasya");
        userRepository.save(user);

        var task = new Task();
        task.setName("vasya's task");
        taskRepository.save(task);

        task.assignUser(user);
        taskRepository.save(task);

        var actual = userRepository.findByName("vasya").orElseThrow();

        assertEquals(1, actual.getTasks().size());
        var first = actual.getTasks().stream().findFirst().orElseThrow();
        assertEquals(task, first);

        var actualTask = taskRepository.findByName("vasya's task").orElseThrow();
        var firstProject = actualTask.getAssignee();
        assertEquals(user, firstProject);
    }
}
