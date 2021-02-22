package org.fedran.manager.service;

import org.springframework.transaction.annotation.Transactional;
import org.fedran.manager.repository.ProjectRepository;
import org.fedran.manager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.fedran.manager.domain.Project;

import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project create(final String name) {
        final var project = new Project();
        project.setName(name);
        return projectRepository.save(project);
    }

    public Optional<String> findByName(final String name) {
        return projectRepository.findByName(name)
                .map(Project::buildFullString);
    }

    public List<String> findALL() {
        return StreamSupport.stream(projectRepository.findAll().spliterator(), false)
                .map(Project::buildShortString)
                .collect(Collectors.toList());
    }

    public void deleteByName(final String name) {
        projectRepository.deleteByName(name);
    }

    public String assignUser(final String userName, final String projectName) {
        final var optUser = userRepository.findByName(userName);
        if (optUser.isEmpty()) {
            return "can not find user with name " + userName;
        }
        final var optProject = projectRepository.findByName(projectName);
        if (optProject.isEmpty()) {
            return "can not find project with name " + projectName;
        }
        final var project = optProject.get();
        project.addUser(optUser.get());
        projectRepository.save(project);
        return userName + " successfully assigned to " + projectName;
    }
}
