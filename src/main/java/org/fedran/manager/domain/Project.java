package org.fedran.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_projects",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")}
    )
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
        user.getProjects().add(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.getProjects().remove(this);
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
        StringBuilder s = new StringBuilder();
        for (User user : getUsers()) {
            s.append(s.length() == 0 ? user.getName() : ", " + user.getName());
        }
        return s.toString();
    }

    private String buildTasksString() {
        StringBuilder s = new StringBuilder();
        for (Task task : getTasks()) {
            s.append(s.length() == 0 ? task.getName() : ", " + task.getName());
        }
        return s.toString();
    }
}
