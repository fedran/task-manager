package org.fedran.manager.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.fedran.manager.service.ProjectService;

import java.util.Collections;
import java.util.List;

@ShellComponent
public class ProjectCommands {
    private final ProjectService service;

    public ProjectCommands(ProjectService service) {
        this.service = service;
    }

    @ShellMethod("Create project")
    public String createProject(final String name) {
        final var project = service.create(name);
        return "Created project with id - " + project.getProjectId() + " name - " + project.getName();
    }

    @ShellMethod("Find project by name")
    public String findProject(final String name) {
        return service.findByName(name)
                .orElse("project with name " + name + " does not exist");
    }

    @ShellMethod("Find all projects")
    public List<String> allProjects() {
        final var allProjects = service.findALL();
        if (allProjects.isEmpty()) {
            return Collections.singletonList("no existing projects yet");
        }
        return allProjects;
    }

    @ShellMethod("Delete project by name")
    public String deleteProject(final String name) {
        service.deleteByName(name);
        return "Project - " + name + " was successfully deleted";
    }
}
