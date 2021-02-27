package org.fedran.manager.commands;

import org.fedran.manager.domain.Project;
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
                .map(Project::buildFullString)
                .orElse("project with name " + name + " does not exist");
    }

    @ShellMethod("Find all projects")
    public String findAllProjects() {
        return service.findALL();
    }

    @ShellMethod("Delete project by name")
    public String deleteProject(final String name) {
        service.deleteByName(name);
        return "Project - " + name + " was successfully deleted";
    }

    @ShellMethod("Assign user to the project")
    public String assignUser(final String userName, final String projectName) {
        return service.assignUser(userName, projectName);
    }
}
