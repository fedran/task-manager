package org.fedran.manager.repository;

import org.fedran.manager.domain.Task;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {

    Optional<Task> findByName(String name);
}