package com.dcmd.dmiracore.service.project;

import com.dcmd.dmiracore.model.Project;
import com.dcmd.dmiracore.model.Task;
import com.dcmd.dmiracore.model.User;
import com.dcmd.dmiracore.payload.project.request.ProjectCreationRequest;
import com.dcmd.dmiracore.payload.project.request.ProjectUpdateRequest;
import com.dcmd.dmiracore.payload.project.response.ProjectResponse;
import com.dcmd.dmiracore.payload.response.MessageResponse;
import com.dcmd.dmiracore.repository.ProjectRepository;
import com.dcmd.dmiracore.repository.TaskRepository;
import com.dcmd.dmiracore.repository.UserRepository;
import com.dcmd.dmiracore.service.project.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectMapper projectMapper;

    public ResponseEntity<MessageResponse> createProject(ProjectCreationRequest request) {
        if (projectRepository.existsProjectsByName(request.getName())) {
            return buildErrorResponse("Error: Project with given name exists");
        }
        if (projectRepository.existsProjectsByTag(request.getTag())) {
            return buildErrorResponse("Error: Project with given tag exists");
        }

        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project project = new Project(request.getName(), request.getTag(), user);
        projectRepository.save(project);

        return ResponseEntity.ok(new MessageResponse("Project was successfully added"));
    }

    public Set<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(project -> projectMapper.mapEntityToResponse(project))
                .collect(Collectors.toSet());
    }

    public ProjectResponse getProjectByName(String name) {
        Project project = projectRepository.findProjectByName(name)
                .orElseThrow(() -> new RuntimeException(String.format("Project with name %s not found", name)));

        return projectMapper.mapEntityToResponse(project);
    }

    private ResponseEntity<MessageResponse> buildErrorResponse(String message) {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(message));
    }

    public ResponseEntity<?> updateProject(ProjectUpdateRequest request) {
        Project project = projectRepository.findProjectByName(request.getName())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!projectRepository.existsProjectsByName(request.getName())) {
            return buildErrorResponse("Error: Project with given name exists");
        }
        if (!projectRepository.existsProjectsByTag(request.getTag())) {
            return buildErrorResponse("Error: Project with given tag exists");
        }

        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Task> tasks = taskRepository.findTasksByNameIn(request.getTasks());
        Set<User> assignedUsers = updateAssignedUsers(request, project);

        Project updatedProject = Project.Builder.from(project)
                .name(request.getName())
                .tag(request.getTag())
                .modifiedBy(user)
                .tasks(tasks)
                .assignedUsers(assignedUsers)
                .build();

        projectRepository.save(updatedProject);
        ProjectResponse updatedProjectResponse = projectMapper.mapEntityToResponse(updatedProject);

        return ResponseEntity.ok(updatedProjectResponse);
    }

    private Set<User> updateAssignedUsers(ProjectUpdateRequest request, Project project) {
        Set<User> assignedUsers = userRepository.findUserByUsernameIn(request.getAssignedUsers());
        Set<User> updatedAssignedUsers = project.getAssignedUsers();
        updatedAssignedUsers.addAll(assignedUsers);
        return updatedAssignedUsers;
    }

    public MessageResponse deleteProject(String name) {
        Project project = projectRepository.findProjectByName(name)
                .orElseThrow(() -> new RuntimeException(String.format("Project with name %s does not exist", name)));

        taskRepository.deleteAll(project.getTasks());
        projectRepository.delete(project);
        return new MessageResponse(String.format("Project wih name %s is successful removed", name));
    }
}
