package com.dcmd.dmiracore.payload.task;

public class TaskResponse {
    private String id;
    private String name;
    private String description;
    private String projectName;
    private String createdBy;
    private String createDate;
    private String modifiedBy;
    private String modifyDate;
    private String assignedTo;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public String getAssignedTo() {
        return assignedTo;
    }


    public static final class Builder {
        private String id;
        private String name;
        private String description;
        private String projectName;
        private String createdBy;
        private String createDate;
        private String modifiedBy;
        private String modifyDate;
        private String assignedTo;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(String id) {
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

        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder createDate(String createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder modifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this;
        }

        public Builder modifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public Builder assignedTo(String assignedTo) {
            this.assignedTo = assignedTo;
            return this;
        }

        public TaskResponse build() {
            TaskResponse taskResponse = new TaskResponse();
            taskResponse.description = this.description;
            taskResponse.projectName = this.projectName;
            taskResponse.modifiedBy = this.modifiedBy;
            taskResponse.createdBy = this.createdBy;
            taskResponse.modifyDate = this.modifyDate;
            taskResponse.name = this.name;
            taskResponse.id = this.id;
            taskResponse.createDate = this.createDate;
            taskResponse.assignedTo = this.assignedTo;
            return taskResponse;
        }
    }
}
