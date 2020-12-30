package com.dcmd.dmiracore.model;

import com.dcmd.dmiracore.model.enums.EState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    @Size(max = 2048)
    private String description;
    @DBRef
    private Project project;
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
    @NotBlank
    private EState state;
    @DBRef
    private User assignedTo;

    public Task() {
    }

    public Task(String name, String description, User user, Project project, User assignedTo) {
        this.name = name;
        this.description = description;
        this.createdBy = user;
        this.createDate = LocalDateTime.now();
        this.modifiedBy = user;
        this.modifyDate = LocalDateTime.now();
        this.project = project;
        this.state = EState.OPEN;
        this.assignedTo = assignedTo;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Project getProject() {
        return project;
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

    public EState getState() {
        return state;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public static final class Builder {
        private String id;
        private String name;
        private String description;
        private Project project;
        private User createdBy;
        private LocalDateTime createDate;
        private User modifiedBy;
        private LocalDateTime modifyDate;
        private EState state;
        private User assignedTo;

        private Builder() {
        }

        private Builder(Task task) {
            this.id = task.getId();
            this.name = task.getName();
            this.description = task.getDescription();
            this.project = task.getProject();
            this.createdBy = task.getCreatedBy();
            this.createDate = task.getCreateDate();
            this.modifiedBy = task.getModifiedBy();
            this.modifyDate = LocalDateTime.now();
            this.state = task.getState();
            this.assignedTo = task.getAssignedTo();
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(Task task) {
            return new Builder(task);
        }


        private Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder project(Project project) {
            this.project = project;
            return this;
        }

        public Builder createdBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder createDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder modifiedBy(User modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this;
        }

        public Builder modifyDate(LocalDateTime modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public Builder state(EState state) {
            this.state = state;
            return this;
        }

        public Builder assignedTo(User assignedTo) {
            this.assignedTo = assignedTo;
            return this;
        }

        public Task build() {
            Task task = new Task(name, description, createdBy, project, assignedTo);
            task.id = this.id;
            task.assignedTo = this.assignedTo;
            task.modifyDate = this.modifyDate;
            task.modifiedBy = this.modifiedBy;
            task.createdBy = this.createdBy;
            task.createDate = this.createDate;
            task.state = this.state;
            return task;
        }
    }
}
