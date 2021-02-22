package org.fedran.manager.service;

import org.springframework.transaction.annotation.Transactional;
import org.fedran.manager.repository.ProjectRepository;
import org.fedran.manager.repository.UserRepository;
import org.fedran.manager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.fedran.manager.domain.Task;

import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public Task create(final String name) {
        final var task = new Task();
        task.setName(name);
        taskRepository.save(task);
        return task;
    }

    public Optional<String> findByName(final String name) {
        return taskRepository.findByName(name)
                .map(Task::buildFullString);
    }

    public List<String> findAll() {
        return StreamSupport.stream(taskRepository.findAll().spliterator(), false)
                .map(Task::buildShortString)
                .collect(Collectors.toList());
    }

    public void deleteByName(final String name) {
        taskRepository.deleteByName(name);
    }

    public String assignTaskOnUser(final String taskName, final String userName) {
        final var optUser = userRepository.findByName(userName);
        if (optUser.isEmpty()) {
            return "can not find user with name " + userName;

        }
        final var optTask = taskRepository.findByName(taskName);
        if (optTask.isEmpty()) {
            return "can not find task " + taskName;
        }
        final var task = optTask.get();
        task.assignUser(optUser.get());
        taskRepository.save(task);
        return taskName + " assigned on " + userName;
    }

    public String addTaskToProject(final String taskName, final String projectName) {
        final var optTask = taskRepository.findByName(taskName);
        if (optTask.isEmpty()) {
            return "failed to load " + taskName;
        }
        final var optProject = projectRepository.findByName(projectName);
        if (optProject.isEmpty()) {
            return "failed to load " + projectName;
        }
        final var task = optTask.get();
        task.addTo(optProject.get());
        taskRepository.save(task);
        return taskName + " successfully added to " + projectName;
    }

    public List<String> generateReport(final String projectName, final String userName) {
        final var optProject = projectRepository.findByName(projectName);
        if (optProject.isEmpty()) {
            return Collections.singletonList("failed to load project " + projectName);
        }
        final var optUser = userRepository.findByName(userName);
        if (optUser.isEmpty()) {
            return Collections.singletonList("failed to load user " + userName);
        }
        final var report = taskRepository.findByProjectIdAndAssigneeId(optProject.get().getProjectId(), optUser.get().getUserId());
        if (report.isEmpty()) {
            return Collections.singletonList("no available reports for " + projectName + " by " + userName);
        }
        return report.stream()
                .map(Task::buildShortString)
                .collect(Collectors.toList());
    }
}
