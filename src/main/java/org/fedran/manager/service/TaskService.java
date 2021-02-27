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
            return failedToLoadMessage(userName);

        }
        return taskRepository.findByName(taskName)
                .map(task -> {
                    task.assignUser(optUser.get());
                    taskRepository.save(task);
                    return taskName + " assigned on " + userName;
                })
                .orElse(failedToLoadMessage(taskName));
    }

    public String addTaskToProject(final String taskName, final String projectName) {
        final var optTask = taskRepository.findByName(taskName);
        if (optTask.isEmpty()) {
            return failedToLoadMessage(taskName);
        }
        return projectRepository.findByName(projectName)
                .map(project -> {
                    final var task = optTask.get();
                    task.addTo(project);
                    taskRepository.save(task);
                    return taskName + " successfully added to " + projectName;
                })
                .orElse(failedToLoadMessage(projectName));
    }

    public List<String> generateReport(final String projectName, final String userName) {
        final var optProject = projectRepository.findByName(projectName);
        if (optProject.isEmpty()) {
            return Collections.singletonList(failedToLoadMessage(projectName));
        }
        final var optUser = userRepository.findByName(userName);
        if (optUser.isEmpty()) {
            return Collections.singletonList(failedToLoadMessage(userName));
        }
        final var report = taskRepository.findByProjectIdAndAssigneeId(optProject.get().getProjectId(), optUser.get().getUserId());
        if (report.isEmpty()) {
            return Collections.singletonList("no available reports for " + projectName + " by " + userName);
        }
        return report.stream()
                .map(Task::buildShortString)
                .collect(Collectors.toList());
    }

    public String addSubtask(String taskName, String subTaskName) {
        final var optTask = taskRepository.findByName(taskName);
        if (optTask.isEmpty()) {
            return failedToLoadMessage(taskName);
        }
        return taskRepository.findByName(subTaskName)
                .map(subTask -> {
                    final var task = optTask.get();
                    task.addChild(subTask);
                    taskRepository.save(task);
                    return subTaskName + " added to " + taskName;
                })
                .orElse(failedToLoadMessage(subTaskName));
    }

    public String removeParent(final String taskName) {
        return taskRepository.findByName(taskName)
                .map(task -> {
                    task.removeParent();
                    taskRepository.save(task);
                    return "parent of " + taskName + " removed";
                })
                .orElse(failedToLoadMessage(taskName));
    }

    public String closeTask(final String taskName) {
        return taskRepository.findByName(taskName)
                .map(task -> {
                    task.close();
                    taskRepository.save(task);
                    return taskName + " was successfully closed";
                })
                .orElse(failedToLoadMessage(taskName));
    }

    public String estimate(final String taskName, final Integer min) {
        return taskRepository.findByName(taskName)
                .map(task -> {
                    task.setEstimateMin(min);
                    taskRepository.save(task);
                    return taskName + " successfully estimated";
                })
                .orElse(failedToLoadMessage(taskName));
    }

    public String spend(final String taskName, final Integer min) {
        return taskRepository.findByName(taskName)
                .map(task -> {
                    task.setSpendMin(min);
                    taskRepository.save(task);
                    return taskName + " spent time successfully set";
                })
                .orElse(failedToLoadMessage(taskName));
    }

    public String calculateRemainingTimeSum(final String taskName) {
        return taskRepository.findByName(taskName)
                .map(task -> "remaining time: " + task.calculateRemainingTimeSum())
                .orElse(failedToLoadMessage(taskName));
    }

    public static String failedToLoadMessage(final String name) {
        return "failed to load " + name;
    }
}
