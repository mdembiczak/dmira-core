package com.dcmd.dmiracore.payload.project.response;

import java.util.Set;

public class ProjectResponse {
    private String id;
    private String name;
    private String tag;
    private String createdBy;
    private String createDate;
    private String modifiedBy;
    private String modifyDate;
    private Set<String> tasks;
    private Set<String> assignedUsers;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
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

    public Set<String> getTasks() {
        return tasks;
    }

    public Set<String> getAssignedUsers() {
        return assignedUsers;
    }


    public static final class Builder {
        private String id;
        private String name;
        private String tag;
        private String createdBy;
        private String createDate;
        private String modifiedBy;
        private String modifyDate;
        private Set<String> tasks;
        private Set<String> assignedUsers;

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

        public Builder tag(String tag) {
            this.tag = tag;
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

        public Builder tasks(Set<String> tasks) {
            this.tasks = tasks;
            return this;
        }

        public Builder assignedUsers(Set<String> assignedUsers) {
            this.assignedUsers = assignedUsers;
            return this;
        }

        public ProjectResponse build() {
            ProjectResponse projectResponse = new ProjectResponse();
            projectResponse.id = this.id;
            projectResponse.createdBy = this.createdBy;
            projectResponse.modifiedBy = this.modifiedBy;
            projectResponse.modifyDate = this.modifyDate;
            projectResponse.tasks = this.tasks;
            projectResponse.name = this.name;
            projectResponse.assignedUsers = this.assignedUsers;
            projectResponse.createDate = this.createDate;
            projectResponse.tag = this.tag;
            return projectResponse;
        }
    }
}
