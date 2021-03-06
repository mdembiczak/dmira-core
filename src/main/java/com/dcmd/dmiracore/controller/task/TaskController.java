package com.dcmd.dmiracore.controller.task;

import com.dcmd.dmiracore.payload.messages.ErrorMessageResponse;
import com.dcmd.dmiracore.payload.task.TaskCreationRequest;
import com.dcmd.dmiracore.payload.task.TaskResponse;
import com.dcmd.dmiracore.payload.task.TaskStatusUpdateRequest;
import com.dcmd.dmiracore.payload.task.TaskUpdateRequest;
import com.dcmd.dmiracore.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@RequestMapping("/api/task")
public class TaskController {
    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get task by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found given task",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class)))
    })
    @GetMapping("{name}")
    public ResponseEntity<?> getTaskByName(@PathVariable String name, @RequestParam(value = "project_name") String projectName) {
        return taskService.getTaskByNameAndProject(name, projectName);
    }

    @Operation(summary = "Get all tasks from project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found given task",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class))))
    })
    @GetMapping("project/{projectName}")
    public ResponseEntity<?> getTaskByProjectName(@PathVariable String projectName) {
        return taskService.getTaskByProject(projectName);
    }


    @Operation(summary = "Create task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskCreationRequest request) {
        return taskService.createTask(request);
    }

    @Operation(summary = "Update task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Unexpected error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RuntimeException.class)))

    })
    @PutMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskUpdateRequest request) {
        return taskService.updateTask(request);
    }

    @Operation(summary = "Update task status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task status updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "500", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RuntimeException.class)))
    })
    @PutMapping("{name}")
    public ResponseEntity<TaskResponse> updateTaskStatus(@Valid @RequestBody TaskStatusUpdateRequest request, @PathVariable String name) {
        return taskService.updateTaskStatus(request, name);
    }

    @Operation(summary = "Get tasks by assigned user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found tasks for given user",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class))))
    })
    @GetMapping("user/{username}")
    public ResponseEntity<Set<TaskResponse>> getTaskByAssignedTo(@PathVariable String username) {
        return taskService.getTaskAssignedToUser(username);
    }
}
