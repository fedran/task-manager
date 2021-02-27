package org.fedran.manager.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.stream.Collectors;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_projects",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")}
    )
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        getUsers().add(user);
    }

    public void removeUser(User user) {
        getUsers().remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(projectId, project.projectId);
    }

    @Override
    public int hashCode() {
        return projectId != null ? projectId.hashCode() : 0;
    }

    public String buildShortString() {
        return "Project " + getName();
    }

    public String buildFullString() {
        return "Project " + getName() + System.lineSeparator() +
                "Users: " + buildUsersString() + System.lineSeparator() +
                "Tasks: " + buildTasksString();
    }

    private String buildUsersString() {
        return getUsers().stream()
                .map(User::getName)
                .collect(Collectors.joining(", "));
    }

    private String buildTasksString() {
        return getTasks().stream()
                .map(Task::getName)
                .collect(Collectors.joining(", "));
    }
}
