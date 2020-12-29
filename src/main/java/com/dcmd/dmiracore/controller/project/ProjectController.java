package com.dcmd.dmiracore.controller.project;

import com.dcmd.dmiracore.payload.project.request.ProjectCreationRequest;
import com.dcmd.dmiracore.payload.project.request.ProjectUpdateRequest;
import com.dcmd.dmiracore.payload.project.response.ProjectResponse;
import com.dcmd.dmiracore.payload.response.ErrorMessageResponse;
import com.dcmd.dmiracore.payload.response.MessageResponse;
import com.dcmd.dmiracore.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Get all projects")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found all projects",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponse.class)))
    })
    @GetMapping
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Set<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @Operation(summary = "Get project by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found project with name",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @GetMapping("{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name) {
        return projectService.getProjectByName(name);
    }

    @Operation(summary = "Create project")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @PostMapping
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectCreationRequest projectCreationRequest) {
        return projectService.createProject(projectCreationRequest);
    }

    @Operation(summary = "Update project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful update",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageResponse.class))),
    })
    @PutMapping
    public ResponseEntity<?> updateProject(@Valid @RequestBody ProjectUpdateRequest projectUpdateRequest) {
        return projectService.updateProject(projectUpdateRequest);
    }

    @Operation(summary = "Remove project")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful removal",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))),
    })
    @DeleteMapping("{name}")
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable String name) {
        MessageResponse messageResponse = projectService.deleteProject(name);
        return ResponseEntity.ok().body(messageResponse);
    }
}
