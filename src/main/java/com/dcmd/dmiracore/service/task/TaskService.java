package com.dcmd.dmiracore.service.task;

import com.dcmd.dmiracore.model.Project;
import com.dcmd.dmiracore.model.Task;
import com.dcmd.dmiracore.model.User;
import com.dcmd.dmiracore.payload.messages.ErrorMessageResponse;
import com.dcmd.dmiracore.payload.task.TaskCreationRequest;
import com.dcmd.dmiracore.payload.task.TaskResponse;
import com.dcmd.dmiracore.payload.task.TaskUpdateRequest;
import com.dcmd.dmiracore.repository.ProjectRepository;
import com.dcmd.dmiracore.repository.TaskRepository;
import com.dcmd.dmiracore.repository.UserRepository;
import com.dcmd.dmiracore.service.task.mapper.TaskMapper;
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
    TaskMapper taskMapper;

    public ResponseEntity<Set<TaskResponse>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        Set<TaskResponse> taskResponses = tasks.stream()
                .map(task -> taskMapper.mapEntityToResponse(task))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(taskResponses);
    }

    public ResponseEntity<?> getTaskByName(String name) {
        Optional<Task> task = taskRepository.findTasksByName(name);
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
        TaskResponse taskResponse = taskMapper.mapEntityToResponse(task);

        return ResponseEntity.ok().body(taskResponse);
    }

    public ResponseEntity<TaskResponse> updateTask(TaskUpdateRequest request) {
        Task task = taskRepository.findTasksByName(request.getName())
                .orElseThrow(() -> new RuntimeException(String.format("Task with name %s not found", request.getName())));

        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task.Builder taskBuilder = Task.Builder.from(task)
                .description(request.getDescription())
                .modifiedBy(user);

        if (!request.getAssignedTo().isBlank()) {
            User userToAssign = userRepository.findUserByUsername(request.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("Assigment is not possible. User does not exist"));
            taskBuilder.assignedTo(userToAssign);
        }

        Task updatedTask = taskBuilder.build();
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
