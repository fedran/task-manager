package org.fedran.manager.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "estimate_min")
    private Integer estimateMin;

    @Column(name = "spend_min", nullable = false)
    private Integer spendMin = 0;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Task parent;

    @OneToMany(mappedBy = "parent")
    private Set<Task> children;

    public void removeParent() {
        setParent(null);
    }

    public void close() {
        setStatus(Status.CLOSE);
        for (Task task : getChildren()) {
            task.close();
        }
    }

    public int calculateRemainingTimeSum() {
        var i = getEstimateMin() - getSpendMin();
        return i + children.stream()
            .mapToInt(Task::calculateRemainingTimeSum)
            .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId);
    }

    @Override
    public int hashCode() {
        return taskId != null ? taskId.hashCode() : 0;
    }

    public enum Status {
        OPEN, CLOSE
    }

    public String buildShortString() {
        return "Task - " + name;
    }

    public String buildFullString() {
        return "Task{" +
                "taskId=" + getTaskId() +
                ", name='" + getName() + '\'' +
                ", estimateMin=" + getEstimateMin() +
                ", spendMin=" + getSpendMin() +
                ", status=" + getStatus() +
                ", assignee=" + getAssignee() +
                ", project=" + getProject() +
                ", parent=" + getParent() +
                ", children=" + getParent() +
                '}';
    }
}
