package com.dcmd.dmiracore.model;

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
}
