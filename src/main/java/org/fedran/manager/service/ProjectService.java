package org.fedran.manager.service;

import org.springframework.transaction.annotation.Transactional;
import org.fedran.manager.repository.ProjectRepository;
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

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
}
