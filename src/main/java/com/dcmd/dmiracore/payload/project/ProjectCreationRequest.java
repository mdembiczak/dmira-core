package com.dcmd.dmiracore.payload.project;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ProjectCreationRequest {
    @NotBlank
    @Size(max = 30)
    private String name;
    @NotBlank
    @Size(max = 6)
    private String tag;
    @NotBlank
    @Size(max = 20)
    private String username;

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getUsername() {
        return username;
    }
}
 