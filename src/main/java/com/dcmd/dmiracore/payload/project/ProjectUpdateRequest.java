package com.dcmd.dmiracore.payload.project;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class ProjectUpdateRequest {
    @NotBlank
    @Size(max = 30)
    private String name;
    @NotBlank
    @Size(max = 6)
    private String tag;
    @NotBlank
    @Size(max = 20)
    private String username;

    private Set<String> tasks;
    private Set<String> assignedUsers;

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getTasks() {
        return tasks;
    }

    public Set<String> getAssignedUsers() {
        return assignedUsers;
    }
}
