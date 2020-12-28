package com.dcmd.dmiracore.controller.project;

import com.dcmd.dmiracore.payload.project.request.ProjectCreationRequest;
import com.dcmd.dmiracore.payload.project.request.ProjectUpdateRequest;
import com.dcmd.dmiracore.payload.project.response.ProjectResponse;
import com.dcmd.dmiracore.payload.response.MessageResponse;
import com.dcmd.dmiracore.service.project.ProjectService;
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

    @GetMapping
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Set<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("{name}")
    public ResponseEntity<ProjectResponse> getProjectByName(@PathVariable String name) {
        return ResponseEntity.ok(projectService.getProjectByName(name));
    }

    @PostMapping
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> createProject(@Valid @RequestBody ProjectCreationRequest projectCreationRequest) {
        return projectService.createProject(projectCreationRequest);
    }

    @PutMapping
    public ResponseEntity<?> updateProject(@Valid @RequestBody ProjectUpdateRequest projectUpdateRequest) {
        return projectService.updateProject(projectUpdateRequest);
    }

    @DeleteMapping("{name}")
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable String name) {
        MessageResponse messageResponse = projectService.deleteProject(name);
        return ResponseEntity.accepted().body(messageResponse);
    }
}
