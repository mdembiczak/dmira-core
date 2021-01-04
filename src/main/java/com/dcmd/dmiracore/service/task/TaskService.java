package com.dcmd.dmiracore.service.task;

import com.dcmd.dmiracore.model.Project;
import com.dcmd.dmiracore.model.State;
import com.dcmd.dmiracore.model.Task;
import com.dcmd.dmiracore.model.User;
import com.dcmd.dmiracore.model.enums.EState;
import com.dcmd.dmiracore.payload.messages.ErrorMessageResponse;
import com.dcmd.dmiracore.payload.task.TaskCreationRequest;
import com.dcmd.dmiracore.payload.task.TaskResponse;
import com.dcmd.dmiracore.payload.task.TaskStatusUpdateRequest;
import com.dcmd.dmiracore.payload.task.TaskUpdateRequest;
import com.dcmd.dmiracore.repository.ProjectRepository;
import com.dcmd.dmiracore.repository.StateRepository;
import com.dcmd.dmiracore.repository.TaskRepository;
import com.dcmd.dmiracore.repository.UserRepository;
import com.dcmd.dmiracore.service.task.mapper.TaskMapper;
import com.google.common.base.Strings;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    TaskMapper taskMapper;

    public ResponseEntity<Set<TaskResponse>> getTaskAssignedToUser(String username) {
        List<Task> tasks = taskRepository.findAll();
        Set<Task> tasksAssignedToUser = tasks.stream()
                .filter(task -> task.getAssignedTo().getUsername().equals(username))
                .collect(Collectors.toSet());
        Set<TaskResponse> taskResponses = tasksAssignedToUser.stream()
                .map(task -> taskMapper.mapEntityToResponse(task))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(taskResponses);
    }

    public ResponseEntity<Set<TaskResponse>> getTaskByProject(String projectName) {
        Project project = projectRepository.findProjectByName(projectName)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Set<String> taskNames = project.getTasks().stream()
                .map(Task::getName)
                .collect(Collectors.toSet());
        Set<Task> tasksByNames = taskRepository.findTasksByNameIn(taskNames);
        Set<TaskResponse> taskResponses = tasksByNames.stream()
                .map(task -> taskMapper.mapEntityToResponse(task))
                .collect(Collectors.toSet());
        return ResponseEntity.ok(taskResponses);
    }

    public ResponseEntity<?> getTaskByNameAndProject(String name, String projectName) {
        Optional<Task> task = taskRepository.findTasksByName(name).stream()
                .filter(taskEntity -> taskEntity.getProject().getName().equals(projectName))
                .findFirst();
        if (task.isPresent()) {
            TaskResponse taskResponse = taskMapper.mapEntityToResponse(task.get());
            return ResponseEntity.ok(taskResponse);
        }
        return ResponseEntity.badRequest()
                .body(new ErrorMessageResponse("Task with given name does not exist", 400));
    }


    public ResponseEntity<?> createTask(TaskCreationRequest request) {
        Optional<Project> project = projectRepository.findProjectByName(request.getProjectName());
        Either<ErrorMessageResponse, Project> validation = validateProject(request, project);
        if (validation.isLeft()) {
            return ResponseEntity.badRequest().body(validation.getLeft());
        }
        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Task task = new Task(request.getName(), request.getDescription(), user, validation.get(), user);
        taskRepository.save(task);
        updateProject(validation.get(), task);
        TaskResponse taskResponse = taskMapper.mapEntityToResponse(task);

        return ResponseEntity.ok().body(taskResponse);
    }

    private void updateProject(Project project, Task task) {
        Set<Task> tasks = project.getTasks();
        tasks.add(task);
        Project updatedProject = Project.Builder.from(project)
                .tasks(tasks)
                .build();
        projectRepository.save(updatedProject);
    }

    public ResponseEntity<TaskResponse> updateTask(TaskUpdateRequest request) {
        Task task = taskRepository.findTasksByName(request.getName()).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Task with name %s not found", request.getName())));

        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task.Builder taskBuilder = Task.Builder.from(task)
                .description(request.getDescription())
                .modifiedBy(user);


        if (!Strings.isNullOrEmpty(request.getState())) {
            State state = stateRepository.findByState(EState.valueOf(request.getState()))
                    .orElseThrow(() -> new RuntimeException("This state does not exists"));
            taskBuilder.state(state.getState());
        }

        if (!Strings.isNullOrEmpty(request.getAssignedTo())) {
            User userToAssign = userRepository.findUserByUsername(request.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("Assigment is not possible. User does not exist"));
            taskBuilder.assignedTo(userToAssign);
        }

        Task updatedTask = taskBuilder.build();
        taskRepository.save(updatedTask);
        TaskResponse taskResponse = taskMapper.mapEntityToResponse(updatedTask);
        return ResponseEntity.ok(taskResponse);
    }

    public ResponseEntity<TaskResponse> updateTaskStatus(TaskStatusUpdateRequest request, String name) {
        Task task = taskRepository.findTasksByName(name).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Task with name %s not found", name)));

        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task updatedTask = Task.Builder.from(task)
                .state(EState.valueOf(request.getState()))
                .modifiedBy(user)
                .build();
        taskRepository.save(updatedTask);
        TaskResponse taskResponse = taskMapper.mapEntityToResponse(updatedTask);
        return ResponseEntity.ok(taskResponse);
    }

    private Either<ErrorMessageResponse, Project> validateProject(TaskCreationRequest request, Optional<Project> project) {
        if (project.isEmpty()) {
            return Either.left(buildErrorResponse(String.format("Project with name %s does not exists", request.getProjectName())));
        }

        if (taskNameExistInProject(request, project.get())) {
            return Either.left(buildErrorResponse("Task with given name exists in project"));
        }
        return Either.right(project.get());
    }

    private boolean taskNameExistInProject(TaskCreationRequest request, Project project) {
        return project.getTasks().stream()
                .anyMatch(task -> task.getName().equals(request.getName()));
    }

    private ErrorMessageResponse buildErrorResponse(String message) {
        return new ErrorMessageResponse(message, 400);
    }
}
