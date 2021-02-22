package org.fedran.manager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.fedran.manager.domain.Task;

import java.util.Optional;
import java.util.List;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    Optional<Task> findByName(String name);

    void deleteByName(String name);

    @Query(value = "SELECT * FROM tasks t WHERE t.project_id = :projectId and t.user_id = :userId", nativeQuery = true)
    List<Task> findByProjectIdAndAssigneeId(@Param("projectId") Long projectId, @Param("userId") Long assigneeId);
}
