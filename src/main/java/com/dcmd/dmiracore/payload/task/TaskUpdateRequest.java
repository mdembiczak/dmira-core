package com.dcmd.dmiracore.payload.task;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TaskUpdateRequest {
    @NotBlank
    @Size(min = 3, max = 40)
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String username;
    private String assignedTo;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public String getAssignedTo() {
        return assignedTo;
    }
}
