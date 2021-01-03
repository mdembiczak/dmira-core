package com.dcmd.dmiracore.service.project;

import com.dcmd.dmiracore.model.Project;
import com.dcmd.dmiracore.model.Task;
import com.dcmd.dmiracore.model.User;
import com.dcmd.dmiracore.payload.messages.ErrorMessageResponse;
import com.dcmd.dmiracore.payload.messages.MessageResponse;
import com.dcmd.dmiracore.payload.project.ProjectCreationRequest;
import com.dcmd.dmiracore.payload.project.ProjectResponse;
import com.dcmd.dmiracore.payload.project.ProjectUpdateRequest;
import com.dcmd.dmiracore.repository.ProjectRepository;
import com.dcmd.dmiracore.repository.TaskRepository;
import com.dcmd.dmiracore.repository.UserRepository;
import com.dcmd.dmiracore.service.project.mapper.ProjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    final ProjectRepository projectRepository;

    final TaskRepository taskRepository;

    final UserRepository userRepository;

    final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
    }

    public ResponseEntity<?> createProject(ProjectCreationRequest request) {
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
        ProjectResponse projectResponse = projectMapper.mapEntityToResponse(projectRepository
                .findProjectByName(project.getName()).orElseThrow(() -> new RuntimeException("Project not found.")));

        return ResponseEntity
                .status(201)
                .body(projectResponse);
    }

    public Set<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(projectMapper::mapEntityToResponse)
                .collect(Collectors.toSet());
    }

    public ResponseEntity<?> getProjectByName(String name) {
        Optional<Project> project = projectRepository.findProjectByName(name);
        if (project.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorMessageResponse(String.format("Project with name %s not found", name), 400));
        }

        return ResponseEntity
                .ok()
                .body(projectMapper.mapEntityToResponse(project.get()));
    }

    private ResponseEntity<ErrorMessageResponse> buildErrorResponse(String message) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessageResponse(message, 400));
    }

    public ResponseEntity<?> updateProject(ProjectUpdateRequest request) {
        Optional<Project> project = projectRepository.findProjectByName(request.getName());
        if (project.isEmpty()) {
            return buildErrorResponse("Project not found");
        }

        if (!projectRepository.existsProjectsByTag(request.getTag())) {
            return buildErrorResponse("Project with given tag exists");
        }

        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Task> tasks = taskRepository.findTasksByNameIn(request.getTasks());
        Set<User> assignedUsers = updateAssignedUsers(request, project.get());

        Project updatedProject = Project.Builder.from(project.get())
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
        return new MessageResponse(String.format("Project wih name %s is successful removed", name), 201);
    }

    public ResponseEntity<Set<String>> getUsernamesFromProject(String projectName) {
        Project project = projectRepository.findProjectByName(projectName)
                .orElseThrow(() -> new RuntimeException("Project no found"));
        Set<String> usernames = project.getAssignedUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(usernames);
    }
}
