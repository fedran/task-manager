package org.fedran.manager.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.fedran.manager.service.TaskService;

import java.util.Collections;
import java.util.List;

@ShellComponent
public class TaskCommands {
    private final TaskService service;

    public TaskCommands(TaskService service) {
        this.service = service;
    }

    @ShellMethod("Create task")
    public String createTask(final String name) {
        service.create(name);
        return name + " task created";
    }

    @ShellMethod("Find task")
    public String findTask(final String name) {
        return service.findByName(name)
                .orElse("task " + name + " does not exist");
    }

    @ShellMethod("Find all tasks")
    public List<String> findAllTasks() {
        final var tasks = service.findAll();
        if (tasks.isEmpty()) {
            return Collections.singletonList("no existing tasks yet");
        }
        return tasks;
    }

    @ShellMethod("Delete task by name")
    public String deleteTask(final String name) {
        service.deleteByName(name);
        return name + " successfully deleted";
    }

    @ShellMethod("Assign task on user")
    public String assignTaskOnUser(final String taskName, final String userName) {
        return service.assignTaskOnUser(taskName, userName);
    }

    @ShellMethod("Add to project")
    public String addTaskToProject(final String taskName, final String projectName) {
        return service.addTaskToProject(taskName, projectName);
    }

    @ShellMethod("Generate the report of all tasks for specified Projects by specified User")
    public List<String> generateReport(final String projectName, final String userName) {
        return service.generateReport(projectName, userName);
    }
}
