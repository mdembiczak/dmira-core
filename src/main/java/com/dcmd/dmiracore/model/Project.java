package com.dcmd.dmiracore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "projects")
public class Project {
    @Id
    private String id;
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;
    @NotBlank
    @Size(min = 2, max = 6)
    private String tag;
    @DBRef
    private User createdBy;
    @NotBlank
    @Size(max = 20)
    private LocalDateTime createDate;
    @DBRef
    private User modifiedBy;
    @NotBlank
    @Size(max = 20)
    private LocalDateTime modifyDate;
    @DBRef
    private Set<Task> tasks;
    @DBRef
    private Set<User> assignedUsers;

    public Project() {
    }

    public Project(String name, String tag, User user) {
        this.name = name;
        this.tag = tag;
        this.createdBy = user;
        this.createDate = LocalDateTime.now();
        this.modifiedBy = user;
        this.modifyDate = LocalDateTime.now();
        this.tasks = new HashSet<>();
        this.assignedUsers = new HashSet<>();
    }

    public Project(String name, String tag) {
        this.name = name;
        this.tag = tag;
        this.createDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
        this.tasks = new HashSet<>();
        this.assignedUsers = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Set<User> getAssignedUsers() {
        return assignedUsers;
    }

    public static final class Builder {
        private String id;
        private String name;
        private String tag;
        private User createdBy;
        private LocalDateTime createDate;
        private User modifiedBy;
        private LocalDateTime modifyDate;
        private Set<Task> tasks = new HashSet<>();
        private Set<User> assignedUsers = new HashSet<>();

        private Builder() {
        }

        private Builder(Project project) {
            id = project.id;
            name = project.name;
            tag = project.tag;
            createdBy = project.createdBy;
            createDate = project.createDate;
            modifiedBy = project.modifiedBy;
            modifyDate = LocalDateTime.now();
            tasks.addAll(project.tasks);
            assignedUsers.addAll(project.assignedUsers);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(Project project) {
            return new Builder(project);
        }

        private Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder createdBy(User user) {
            this.createdBy = user;
            return this;
        }

        private Builder createDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder modifiedBy(User user) {
            this.modifiedBy = user;
            return this;
        }

        private Builder modifyDate(LocalDateTime modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public Builder tasks(Set<Task> tasks) {
            this.tasks = tasks;
            return this;
        }

        public Builder assignedUsers(Set<User> assignedUsers) {
            this.assignedUsers = assignedUsers;
            return this;
        }

        public Project build() {
            Project project = new Project(name, tag);
            project.tasks = this.tasks;
            project.createdBy = this.createdBy;
            project.createDate = this.createDate;
            project.id = this.id;
            project.modifiedBy = this.modifiedBy;
            project.modifyDate = this.modifyDate;
            project.assignedUsers = this.assignedUsers;
            return project;
        }
    }
}
