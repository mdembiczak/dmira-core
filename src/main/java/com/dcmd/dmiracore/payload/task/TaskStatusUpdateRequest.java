package com.dcmd.dmiracore.payload.task;

import javax.validation.constraints.NotBlank;

public class TaskStatusUpdateRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String state;
    @NotBlank
    private String projectName;

    public String getUsername() {
        return username;
    }

    public String getState() {
        return state;
    }

    public String getProjectName() {
        return projectName;
    }
}
