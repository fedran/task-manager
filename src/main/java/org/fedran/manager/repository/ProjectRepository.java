package org.fedran.manager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.fedran.manager.domain.Project;

import java.util.Optional;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long> {
    Optional<Project> findByName(String name);

    void deleteByName(String name);
}
